from pathlib import Path
from reportlab.lib import colors
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, PageBreak, Table, TableStyle, Image, Flowable

ROOT = Path(__file__).resolve().parents[2]
PDF = Path(__file__).resolve().parent / "Semana 13.pdf"
EVIDENCE = ROOT / "evidencias" / "01-Eureka-Registro-1.png"
FONT_DIR = Path("C:/Windows/Fonts")
pdfmetrics.registerFont(TTFont("Arial", str(FONT_DIR / "arial.ttf")))
pdfmetrics.registerFont(TTFont("Arial-Bold", str(FONT_DIR / "arialbd.ttf")))
pdfmetrics.registerFont(TTFont("Consolas", str(FONT_DIR / "consola.ttf")))

RED = colors.HexColor("#B42318")
BLACK = colors.HexColor("#171717")
GREY = colors.HexColor("#777777")
LIGHT = colors.HexColor("#D8D8D8")

styles = getSampleStyleSheet()
styles.add(ParagraphStyle(name="TitleRed", fontName="Arial-Bold", fontSize=27, leading=32,
                          textColor=RED, alignment=1, spaceAfter=7))
styles.add(ParagraphStyle(name="Subtitle", fontName="Arial-Bold", fontSize=17, leading=22,
                          textColor=BLACK, alignment=1, spaceAfter=18))
styles.add(ParagraphStyle(name="H1Red", fontName="Arial-Bold", fontSize=16, leading=20,
                          textColor=RED, spaceBefore=8, spaceAfter=8, keepWithNext=True))
styles.add(ParagraphStyle(name="H2Red", fontName="Arial-Bold", fontSize=11.5, leading=15,
                          textColor=RED, spaceBefore=6, spaceAfter=4, keepWithNext=True))
styles.add(ParagraphStyle(name="BodyBlack", fontName="Arial", fontSize=9.4, leading=13.2,
                          textColor=BLACK, spaceAfter=6))
styles.add(ParagraphStyle(name="SmallBlack", fontName="Arial", fontSize=8, leading=10.5,
                          textColor=BLACK, spaceAfter=4))
styles.add(ParagraphStyle(name="Caption", fontName="Arial", fontSize=8, leading=10,
                          textColor=GREY, alignment=1, spaceBefore=5))
styles.add(ParagraphStyle(name="TableHead", fontName="Arial-Bold", fontSize=8.2, leading=10, textColor=RED))
styles.add(ParagraphStyle(name="TableCell", fontName="Arial", fontSize=8, leading=10, textColor=BLACK))

def P(text, style="BodyBlack"):
    return Paragraph(text, styles[style])

def cell(text, head=False):
    return Paragraph(text, styles["TableHead" if head else "TableCell"])

class CodePanel(Flowable):
    def __init__(self, title, lines, width=515, font_size=7.2, leading=9.4):
        super().__init__()
        self.title = title
        self.lines = lines.splitlines()
        self.width = width
        self.font_size = font_size
        self.leading = leading
        self.height = 20 + len(self.lines) * leading + 10
    def wrap(self, avail_w, avail_h):
        self.width = min(self.width, avail_w)
        return self.width, self.height
    def draw(self):
        c = self.canv
        c.saveState()
        c.setStrokeColor(LIGHT)
        c.setLineWidth(0.6)
        c.rect(0, 0, self.width, self.height, stroke=1, fill=0)
        c.setFillColor(RED)
        c.setFont("Arial-Bold", 8.2)
        c.drawString(8, self.height - 13, self.title)
        c.setFillColor(BLACK)
        c.setFont("Consolas", self.font_size)
        y = self.height - 25
        for line in self.lines:
            c.drawString(8, y, line[:145])
            y -= self.leading
        c.restoreState()

class ArchitectureDiagram(Flowable):
    def __init__(self, width=515, height=450):
        super().__init__()
        self.width, self.height = width, height
    def box(self, c, x, y, w, h, title, subtitle="", font=8.5):
        c.setStrokeColor(colors.HexColor("#555555"))
        c.setLineWidth(0.8)
        c.rect(x, y, w, h, stroke=1, fill=0)
        c.setFillColor(RED)
        c.setFont("Arial-Bold", font)
        c.drawCentredString(x + w / 2, y + h - 15, title)
        if subtitle:
            c.setFillColor(BLACK)
            c.setFont("Arial", 7.4)
            c.drawCentredString(x + w / 2, y + 9, subtitle)
    def arrow(self, c, x1, y1, x2, y2, dashed=False):
        import math
        c.saveState()
        c.setStrokeColor(colors.HexColor("#555555"))
        c.setLineWidth(0.8)
        if dashed:
            c.setDash(3, 2)
        c.line(x1, y1, x2, y2)
        angle = math.atan2(y2-y1, x2-x1)
        size = 5
        c.line(x2, y2, x2-size*math.cos(angle-0.45), y2-size*math.sin(angle-0.45))
        c.line(x2, y2, x2-size*math.cos(angle+0.45), y2-size*math.sin(angle+0.45))
        c.restoreState()
    def draw(self):
        c = self.canv
        c.saveState()
        self.box(c, 12, 375, 112, 48, "Cliente HTTP", "Postman / navegador")
        self.box(c, 194, 375, 130, 48, "API Gateway", "puerto 8080")
        self.box(c, 391, 375, 112, 48, "Eureka Server", "puerto 8761")
        self.arrow(c, 124, 399, 194, 399)
        c.setFillColor(BLACK); c.setFont("Arial", 7.5); c.drawCentredString(159, 406, "solicitud")
        self.arrow(c, 324, 399, 391, 399, True)
        c.drawCentredString(357, 406, "descubre")
        self.box(c, 10, 220, 115, 58, "categoria-service", "puerto 8082")
        self.box(c, 140, 220, 115, 58, "producto-service", "puerto 8083")
        self.box(c, 270, 220, 115, 58, "cliente-service", "puerto 8081")
        self.box(c, 400, 220, 105, 58, "pedido-service", "puerto 8084", font=8)
        c.setStrokeColor(GREY); c.setLineWidth(0.7)
        c.line(259, 375, 259, 330); c.line(67, 330, 452, 330)
        centers = [67.5, 197.5, 327.5, 452.5]
        for cx in centers:
            self.arrow(c, cx, 330, cx, 278)
        for cx in centers:
            self.arrow(c, cx, 284, 447, 375, True)
        self.box(c, 10, 112, 115, 42, "MySQL", "bd_categoria_dev", font=8)
        self.box(c, 140, 112, 115, 42, "MySQL", "bd_producto_dev", font=8)
        self.box(c, 270, 112, 115, 42, "MySQL", "bd_cliente_dev", font=8)
        self.box(c, 400, 112, 105, 42, "MySQL", "bd_pedido_dev", font=8)
        for cx in centers:
            self.arrow(c, cx, 220, cx, 154)
        c.setFillColor(BLACK); c.setFont("Arial", 8)
        c.drawCentredString(258, 75, "Cada servicio conserva su propio esquema de datos")
        c.drawCentredString(258, 60, "Pedidos consulta clientes y productos mediante REST usando sus IDs")
        c.restoreState()

doc = SimpleDocTemplate(str(PDF), pagesize=A4, leftMargin=40, rightMargin=40,
                        topMargin=38, bottomMargin=36,
                        title="Laboratorio 13 - Service Discovery y API Gateway",
                        author="Jesus Castillo Sumire")
story = []

story += [Spacer(1, 32), P("LABORATORIO 13", "TitleRed"),
          P("Service Discovery y API Gateway", "Subtitle"),
          P("Jesus Castillo Sumire<br/>Desarrollo de Aplicaciones Web - C24-D", "BodyBlack"),
          Spacer(1, 8), P("Presentación", "H1Red"),
          P("En este laboratorio integré Eureka y Spring Cloud Gateway con los servicios de categorías, productos, clientes y pedidos. La arquitectura permite registrar las aplicaciones en un punto común y atender las solicitudes desde una entrada HTTP centralizada."),
          P("Objetivo", "H1Red"),
          P("Implementar el descubrimiento de servicios y publicar las APIs de clientes y pedidos mediante el Gateway, manteniendo disponibles las rutas de categorías y productos. Comprobé el registro en Eureka y el acceso HTTP a cada ruta."),
          P("Cómo lo desarrollé", "H1Red"),
          P("Preparé los proyectos Spring Boot con Java 17, agregué Eureka Server, configuré los clientes para registrarse, declaré las rutas del Gateway con destinos <font name='Consolas'>lb://</font> y ejecuté los seis procesos. Para la persistencia usé MySQL de XAMPP en el puerto 3306, con una base independiente para cada servicio de negocio."),
          P("Herramientas y entorno", "H1Red"),
          Table([[cell("Herramienta", True), cell("Uso", True)],
                 [cell("Visual Studio Code y Maven Wrapper"), cell("Edición, compilación y ejecución de los servicios")],
                 [cell("Spring Boot 3.5.15, Spring Cloud 2025.0.0, Java 17"), cell("Aplicaciones, registro, enrutamiento y balanceo")],
                 [cell("XAMPP / MySQL"), cell("Persistencia local en el puerto 3306")],
                 [cell("Navegador y solicitudes HTTP"), cell("Eureka, rutas del Gateway y estado de salud")]],
                colWidths=[190, 325], style=TableStyle([
                    ("GRID", (0,0), (-1,-1), 0.45, LIGHT), ("VALIGN", (0,0), (-1,-1), "TOP"),
                    ("LEFTPADDING", (0,0), (-1,-1), 6), ("RIGHTPADDING", (0,0), (-1,-1), 6),
                    ("TOPPADDING", (0,0), (-1,-1), 5), ("BOTTOMPADDING", (0,0), (-1,-1), 5)])),
          Spacer(1, 10), P("Resultado", "H1Red"),
          P("Eureka mostró las cinco aplicaciones registradas como UP. Las cuatro rutas de negocio respondieron HTTP 200 a través del Gateway y el endpoint de salud devolvió status UP. La compilación Maven terminó correctamente; las pruebas automatizadas se dejaron omitidas durante esa compilación.")]

story += [PageBreak(), P("Arquitectura y flujo", "H1Red"),
          P("El cliente envía las solicitudes al puerto 8080. El Gateway consulta el registro de Eureka y reenvía cada ruta al servicio correspondiente. Cada servicio de negocio persiste sus propios datos en su esquema MySQL."),
          ArchitectureDiagram(), P("Flujo de una solicitud", "H2Red"),
          P("1. El cliente llama una ruta del Gateway, por ejemplo <font name='Consolas'>/api/pedidos</font>.<br/>2. El Gateway resuelve el destino registrado como <font name='Consolas'>pedido-service</font>.<br/>3. El microservicio procesa la operación y usa su repositorio para acceder a su base de datos.<br/>4. Para validar referencias de un pedido, Pedido Service consulta los servicios de clientes y productos por REST con los identificadores recibidos."),
          P("Separación de datos", "H2Red"),
          P("Las bases están separadas: los IDs de cliente y producto se transportan en la solicitud del pedido. No existe una clave foránea entre esquemas de diferentes servicios.")]

story += [PageBreak(), P("Configuración implementada", "H1Red"),
          P("Eureka se ejecuta en el puerto 8761 y funciona como registro central. No se registra a sí mismo ni necesita consumir su propio catálogo."),
          CodePanel("eureka-server/src/main/resources/application.yml", """server:
  port: 8761
spring:
  application:
    name: eureka-server
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false"""),
          Spacer(1, 8),
          P("La anotación <font name='Consolas'>@EnableEurekaServer</font> activa el servidor de descubrimiento.", "SmallBlack"),
          CodePanel("EurekaServerApplication.java", """@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}"""),
          Spacer(1, 9),
          P("El Gateway define rutas explícitas para los cuatro servicios. El prefijo <font name='Consolas'>lb://</font> indica que el destino se resuelve por nombre registrado.", "BodyBlack"),
          CodePanel("gateway-service/src/main/resources/application.yml", """server:
  port: 8080
spring:
  application:
    name: gateway-service
  cloud:
    gateway:
      routes:
        - id: categoria-service
          uri: lb://categoria-service
          predicates:
            - Path=/api/categorias/**
        - id: producto-service
          uri: lb://producto-service
          predicates:
            - Path=/api/productos/**
        - id: cliente-service
          uri: lb://cliente-service
          predicates:
            - Path=/api/clientes/**
        - id: pedido-service
          uri: lb://pedido-service
          predicates:
            - Path=/api/pedidos/**""")]

story += [PageBreak(), P("Persistencia y validaciones", "H1Red"),
          P("Los servicios de categorías, productos, clientes y pedidos cargan el perfil dev. La configuración local permite crear la base si todavía no existe; Hibernate mantiene el esquema actualizado."),
          CodePanel("pedido-service/src/main/resources/application-dev.yml", """server:
  port: 8084
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bd_pedido_dev?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password: (vacía por defecto en XAMPP)
  jpa:
    hibernate:
      ddl-auto: update
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
pedido:
  url-clientes: http://localhost:8081
  url-productos: http://localhost:8083"""),
          P("El servicio de pedidos verifica que cliente y productos existan antes de guardar; calcula el total a partir del precio y la cantidad. Estas llamadas internas usan las URLs configurables anteriores. El enrutamiento del Gateway, por su parte, sí usa los nombres de Eureka.", "SmallBlack"),
          CodePanel("PedidoService.java - fragmentos reales", """clientes=b.clone().baseUrl(c).build();
productos=b.clone().baseUrl(p).build();
clientes.get().uri(\"/api/clientes/{id}\",d.idCliente()).retrieve().toBodilessEntity();
productos.get().uri(\"/api/productos/{id}\",x.idProducto()).retrieve().toBodilessEntity();
total=total.add(x.precio().multiply(BigDecimal.valueOf(x.cantidad())));"""),
          Spacer(1, 8), P("Verificación HTTP realizada", "H2Red"),
          Table([[cell("Solicitud al Gateway", True), cell("Estado", True), cell("Respuesta observada", True)],
                 [cell("GET /api/categorias"), cell("200"), cell("Una categoría devuelta")],
                 [cell("GET /api/productos"), cell("200"), cell("Producto asociado a categoría ID 1")],
                 [cell("GET /api/clientes"), cell("200"), cell("Cliente de demostración devuelto")],
                 [cell("GET /api/pedidos"), cell("200"), cell("Pedido con total 37.00 y 2 unidades")],
                 [cell("GET /actuator/health"), cell("200"), cell("status: UP")]],
                colWidths=[190, 55, 270], style=TableStyle([
                    ("GRID", (0,0), (-1,-1), 0.45, LIGHT), ("VALIGN", (0,0), (-1,-1), "TOP"),
                    ("LEFTPADDING", (0,0), (-1,-1), 6), ("RIGHTPADDING", (0,0), (-1,-1), 6),
                    ("TOPPADDING", (0,0), (-1,-1), 5), ("BOTTOMPADDING", (0,0), (-1,-1), 5)])),
          Spacer(1, 6),
          P("Los resultados se obtuvieron mediante solicitudes HTTP reales contra localhost durante la ejecución del proyecto.", "SmallBlack")]

story += [PageBreak(), P("Evidencia del registro de servicios", "H1Red"),
          P("Captura exportada desde la interfaz real de Eureka en localhost:8761. Se observan los cinco registros: Gateway y los cuatro microservicios de negocio.", "BodyBlack")]
img = Image(str(EVIDENCE))
img._restrictSize(480, 650)
img.hAlign = "CENTER"
story += [img, P("Eureka muestra las aplicaciones como UP. En la parte superior aparece el aviso de autoprotección y renovaciones de Eureka; también comprobé las rutas del Gateway y su endpoint de salud, que respondieron HTTP 200.", "Caption")]

story += [PageBreak(), P("Ejecución y conclusiones", "H1Red"),
          P("Para reproducir la validación", "H2Red"),
          P("1. Iniciar MySQL desde XAMPP.<br/>2. Abrir seis terminales en la carpeta del proyecto.<br/>3. Ejecutar en orden: Eureka, categoría, producto, cliente, pedido y Gateway.<br/>4. Revisar <font name='Consolas'>http://localhost:8761</font> y consultar las rutas en <font name='Consolas'>http://localhost:8080</font>.<br/>5. Confirmar las cuatro respuestas 200 y que <font name='Consolas'>/actuator/health</font> indique UP."),
          P("Conclusiones", "H2Red")]
for conclusion in [
    "Comprendí que Eureka mantiene un registro común y evita que el Gateway dependa de direcciones fijas para enrutar las solicitudes externas.",
    "Al centralizar las rutas en el Gateway, pude llamar categorías, productos, clientes y pedidos desde un mismo puerto.",
    "La separación de las bases de datos permite que cada microservicio administre su persistencia sin compartir tablas con los demás.",
    "Las referencias entre pedidos, clientes y productos se conservan mediante identificadores y validaciones REST, no mediante claves foráneas entre esquemas.",
    "La validación HTTP 200 y el estado UP confirmaron el registro y el enrutamiento; también aprendí a revisar el aviso de autoprotección de Eureka junto con otras pruebas de salud."
]:
    story.append(P("• " + conclusion, "BodyBlack"))
story += [Spacer(1, 8), P("Alcance de las pruebas", "H2Red"),
          P("La compilación se realizó con Maven y finalizó en BUILD SUCCESS. En esa compilación se omitieron las pruebas automatizadas; las comprobaciones documentadas son las solicitudes HTTP manuales ejecutadas durante el laboratorio.", "SmallBlack")]

doc.build(story)
print(PDF)

