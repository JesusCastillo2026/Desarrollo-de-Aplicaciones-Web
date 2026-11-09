from pathlib import Path
from reportlab.lib import colors
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.units import mm
from reportlab.platypus import (SimpleDocTemplate, Paragraph, Spacer, PageBreak, Image, Table, TableStyle, Preformatted, KeepTogether, Flowable)
from PIL import Image as PILImage

ROOT=Path(__file__).resolve().parents[1]
EVID=ROOT/'evidencias'
CAP=EVID/'capturas'
OUT=EVID/'EVALUACION_03_Gestion_Pedidos_Restaurante.pdf'
RED=colors.HexColor('#B42318')
DARK=colors.HexColor('#202124')
GRAY=colors.HexColor('#6B7280')

styles=getSampleStyleSheet()
styles.add(ParagraphStyle(name='TitleRed', parent=styles['Title'], fontName='Helvetica-Bold', fontSize=25, leading=31, textColor=RED, alignment=TA_CENTER, spaceAfter=12))
styles.add(ParagraphStyle(name='SubTitle', parent=styles['Normal'], fontName='Helvetica', fontSize=14, leading=20, textColor=DARK, alignment=TA_CENTER, spaceAfter=8))
styles.add(ParagraphStyle(name='H1Red', parent=styles['Heading1'], fontName='Helvetica-Bold', fontSize=17, leading=22, textColor=RED, spaceBefore=5, spaceAfter=9, keepWithNext=True))
styles.add(ParagraphStyle(name='H2Red', parent=styles['Heading2'], fontName='Helvetica-Bold', fontSize=12, leading=16, textColor=RED, spaceBefore=8, spaceAfter=5, keepWithNext=True))
styles.add(ParagraphStyle(name='BodyBlack', parent=styles['BodyText'], fontName='Helvetica', fontSize=9.5, leading=14, textColor=colors.black, spaceAfter=6))
styles.add(ParagraphStyle(name='Caption', parent=styles['BodyText'], fontName='Helvetica-Oblique', fontSize=8, leading=10, textColor=DARK, alignment=TA_CENTER, spaceBefore=3, spaceAfter=9))
styles.add(ParagraphStyle(name='Small', parent=styles['BodyText'], fontName='Helvetica', fontSize=8, leading=11, textColor=colors.black, spaceAfter=3))
styles.add(ParagraphStyle(name='CodeLabel', parent=styles['BodyText'], fontName='Helvetica-Bold', fontSize=8, leading=10, textColor=GRAY, spaceBefore=2, spaceAfter=3))

class Architecture(Flowable):
    def __init__(self, width=510, height=170):
        Flowable.__init__(self); self.width=width; self.height=height
    def draw(self):
        c=self.canv
        c.setStrokeColor(colors.HexColor('#737373')); c.setLineWidth(0.8); c.setFillColor(colors.black)
        boxes=[(8,101,145,44,'CLIENTE','REST :8081','bd_cliente / clientes'),(182,101,145,44,'PLATO','REST :8082','bd_plato / platos'),(357,101,145,44,'PEDIDO','REST :8083','bd_pedido / pedidos + detalle_pedido')]
        for x,y,w,h,a,b,d in boxes:
            c.rect(x,y,w,h,stroke=1,fill=0)
            c.setFont('Helvetica-Bold',9); c.drawCentredString(x+w/2,y+h-13,a)
            c.setFont('Helvetica',7.5); c.drawCentredString(x+w/2,y+h-25,b)
            c.setFont('Helvetica',7); c.drawCentredString(x+w/2,y+h-36,d)
        # REST references from Pedido to the master services
        c.setStrokeColor(RED); c.setLineWidth(1.1)
        c.line(360,124,330,124); c.line(185,124,155,124)
        for x in (155,330):
            c.line(x,124,x+6,128); c.line(x,124,x+6,120)
        c.setFillColor(DARK); c.setFont('Helvetica-Oblique',7)
        c.drawCentredString(342,132,'REST consulta cliente/plato')
        c.drawCentredString(166,132,'referencia externa')
        c.setStrokeColor(colors.HexColor('#737373'))
        c.setFont('Helvetica',8)
        c.drawCentredString(255,73,'Cada servicio persiste en su propia base MySQL; no hay FK entre servicios.')
        c.drawCentredString(255,59,'Pedido valida cliente y platos, toma el precio vigente y calcula subtotales y total.')
        c.setStrokeColor(RED); c.line(430,101,430,89); c.line(430,89,430,80)
        c.line(425,85,430,80); c.line(435,85,430,80)

def para(text, style='BodyBlack'):
    return Paragraph(text, styles[style])
def codeblock(text):
    # Literal source extract, clearly labeled as code and not as an app screenshot.
    p=Preformatted(text.strip('\n'), ParagraphStyle('CodeTmp', fontName='Courier', fontSize=7.3, leading=9.2, textColor=colors.black, leftIndent=7, rightIndent=7, borderWidth=0.5, borderColor=colors.HexColor('#999999'), borderPadding=6, spaceBefore=2, spaceAfter=7))
    return p
def img(path, maxw=500, maxh=360):
    im=Image(str(path))
    scale=min(maxw/im.imageWidth,maxh/im.imageHeight)
    im.drawWidth=im.imageWidth*scale; im.drawHeight=im.imageHeight*scale
    im.hAlign='CENTER'
    return im
def screenshot(path, caption, maxw=500, maxh=350):
    return KeepTogether([img(CAP/path,maxw,maxh),para(caption,'Caption')])

doc=SimpleDocTemplate(str(OUT),pagesize=A4,rightMargin=18*mm,leftMargin=18*mm,topMargin=16*mm,bottomMargin=16*mm,title='Evaluacion 03 - Gestion de pedidos de restaurante',author='Jesus Castillo Sumire')
story=[]

# 1 - Cover and brief
story += [Spacer(1,20*mm),para('EVALUACION 03','TitleRed'),para('Gestion de pedidos de restaurante','SubTitle'),Spacer(1,7*mm),para('Jesus Jose Castillo Sumire','SubTitle'),para('Curso: Desarrollo de Aplicaciones Web','SubTitle'),para('Noviembre de 2026','SubTitle'),Spacer(1,10*mm),para('Objetivo','H1Red'),para('Construir una API distribuida en tres servicios Spring Boot para administrar clientes, platos y pedidos. Cada servicio conserva sus datos en una base MySQL independiente; el servicio de pedidos consulta a Cliente y Plato por REST para validar referencias y calcular importes.','BodyBlack'),para('Como se trabajo','H1Red'),para('Organice el proyecto en tres modulos Maven. Defini entidades JPA, repositorios y capas de servicio/controlador. Configure perfiles YAML de desarrollo y produccion, conecte MySQL de XAMPP y ejecute la coleccion de pruebas desde Postman. La evidencia visual de este informe proviene de Postman, XAMPP, phpMyAdmin y Visual Studio Code.','BodyBlack'),para('Resultado comprobable','H1Red'),para('<b>Postman:</b> 28 aserciones automatizadas aprobadas, 0 fallidas y 0 errores. <b>Persistencia:</b> tres bases separadas; tablas visibles en phpMyAdmin. <b>Proyecto:</b> tres servicios y sus carpetas se ven en VS Code.','BodyBlack'),Spacer(1,4*mm),para('Asignacion individual','H2Red'),para('El PDF Evaluacion 03 - D asigna a CASTILLO SUMIRE, JESUS JOSE el laboratorio individual N. 4: Pedidos de restaurante, con los microservicios Cliente, Plato y Pedido. Este proyecto y sus evidencias corresponden a esa asignacion.','Small'),PageBreak()]

# 2 - Architecture and flow
story += [para('Arquitectura y flujo','H1Red'),para('Los tres servicios se ejecutan de forma independiente. Cliente y Plato publican sus operaciones CRUD; Pedido almacena las solicitudes y sus detalles. Las referencias del cliente y del plato son identificadores remotos, no claves foraneas entre bases diferentes.','BodyBlack'),Architecture(),Spacer(1,4*mm),para('Flujo principal del pedido','H2Red'),para('1. Postman envia el pedido a Pedido (puerto 8083).  2. Pedido consulta Cliente (8081) y verifica que exista y este activo.  3. Pedido consulta cada Plato (8082), comprueba disponibilidad y lee el precio vigente.  4. Calcula subtotal = precio x cantidad y suma los subtotales.  5. Guarda pedido y detalles en bd_pedido y devuelve la respuesta REST.','BodyBlack'),para('Servicios y bases de datos','H2Red')]
rows=[['Servicio','Puerto','Base de datos / tablas'],['Cliente','8081','bd_cliente / clientes'],['Plato','8082','bd_plato / platos'],['Pedido','8083','bd_pedido / pedidos, detalle_pedido']]
t=Table(rows,colWidths=[100,65,300],repeatRows=1)
t.setStyle(TableStyle([('TEXTCOLOR',(0,0),(-1,-1),colors.black),('FONTNAME',(0,0),(-1,0),'Helvetica-Bold'),('FONTNAME',(0,1),(-1,-1),'Helvetica'),('FONTSIZE',(0,0),(-1,-1),8.5),('LEADING',(0,0),(-1,-1),12),('LINEBELOW',(0,0),(-1,0),0.8,RED),('LINEBELOW',(0,1),(-1,-1),0.3,colors.HexColor('#B7B7B7')),('VALIGN',(0,0),(-1,-1),'MIDDLE'),('LEFTPADDING',(0,0),(-1,-1),5),('RIGHTPADDING',(0,0),(-1,-1),5),('TOPPADDING',(0,0),(-1,-1),6),('BOTTOMPADDING',(0,0),(-1,-1),6)]))
story += [t,Spacer(1,4*mm),para('La aplicacion no necesita Apache para servir sus APIs: Spring Boot atiende en 8081, 8082 y 8083. XAMPP se utiliza aqui para ejecutar MariaDB/MySQL en el puerto 3306.','Small'),PageBreak()]

# 3 - Implementation, code, profiles and execution
story += [para('Implementacion tecnica','H1Red'),para('Cada servicio mantiene la separacion Controller - Service - Repository. Los controladores reciben solicitudes HTTP, los servicios aplican reglas de negocio y los repositorios usan Spring Data JPA/Hibernate para guardar y consultar entidades. DTO separan los datos recibidos y devueltos del modelo persistente.','BodyBlack'),para('Pedido y sus detalles','H2Red'),para('La entidad Pedido se relaciona con sus detalles con @OneToMany; DetallePedido referencia al pedido con @ManyToOne. Los campos idCliente e idPlato conservan referencias a otros servicios como valores, sin relacion JPA entre bases.','BodyBlack'),para('Fragmento literal de PedidoService - consulta y calculo','CodeLabel'),codeblock('''BigDecimal subtotal = plato.precio()
    .multiply(BigDecimal.valueOf(solicitud.cantidad()));
DetallePedido detalle = new DetallePedido();
detalle.setIdPlato(plato.idPlato());
detalle.setNombrePlato(plato.nombre());
detalle.setCantidad(solicitud.cantidad());
detalle.setPrecioUnitario(plato.precio());
detalle.setSubtotal(subtotal);
pedido.addDetalle(detalle);
total = total.add(subtotal);
pedido.setTotal(total);'''),para('Este bloque usa el precio que responde el servicio Plato, persiste el detalle y acumula el total. La clase PedidoService tambien rechaza platos repetidos dentro de un mismo pedido, platos no disponibles y referencias inexistentes.','BodyBlack'),para('Configuracion de base y perfiles','H2Red'),para('En dev, cada URL JDBC incluye createDatabaseIfNotExist=true y Hibernate usa ddl-auto=update. Esto permite crear la base al arrancar cuando MySQL da permiso y crear/actualizar tablas. En prod se requieren variables de entorno y ddl-auto=validate, para comprobar el esquema sin alterarlo.','BodyBlack'),para('Arranque local','H2Red'),codeblock(r'''# En tres terminales desde la carpeta del proyecto
.\mvnw.cmd -pl cliente-service spring-boot:run
.\mvnw.cmd -pl plato-service spring-boot:run
.\mvnw.cmd -pl pedido-service spring-boot:run'''),para('Herramientas: Java 17, Spring Boot, Spring Web, Spring Data JPA, Hibernate, MySQL/MariaDB de XAMPP, Maven Wrapper, Visual Studio Code y Postman.','Small'),PageBreak()]

# 4 - DB screenshots
story += [para('Evidencia de persistencia - phpMyAdmin','H1Red'),para('La captura del arbol de bases muestra bd_cliente, bd_pedido y bd_plato. Las vistas de tablas confirman clientes (1 fila), platos (2 filas), pedidos (1 fila) y detalle_pedido (2 filas) en el momento de las capturas.','BodyBlack'),screenshot('phpmyadmin-bases.png','Captura real de phpMyAdmin: bases creadas para los tres servicios.',maxw=225,maxh=145),screenshot('phpmyadmin-bd-cliente.png','bd_cliente: tabla clientes, con 1 registro visible.',maxw=510,maxh=95),screenshot('phpmyadmin-bd-plato.png','bd_plato: tabla platos, con 2 registros visibles.',maxw=510,maxh=95),screenshot('phpmyadmin-bd-pedido.png','bd_pedido: pedidos y detalle_pedido, con 1 pedido y 2 detalles.',maxw=510,maxh=105),PageBreak()]

# 5 - VS Code and XAMPP
story += [para('Evidencia del entorno de trabajo','H1Red'),para('La captura de VS Code muestra la carpeta del proyecto y los tres modulos. La captura de XAMPP muestra MariaDB/MySQL activo en el puerto 3306. La ejecucion de las APIs tambien queda demostrada por las respuestas exitosas a localhost:8081, 8082 y 8083 en Postman.','BodyBlack'),screenshot('vscode-proyecto.png','VS Code: estructura del proyecto gestion-pedidos-restaurante y los tres servicios.',maxw=500,maxh=300),screenshot('xampp-mysql-activo.png','XAMPP: MySQL activo en el puerto 3306. Apache permanece detenido porque no se usa para estas APIs.',maxw=500,maxh=325),PageBreak()]

# 6 - real startup evidence for each microservice
story += [para('Ejecucion de microservicios - Cliente y Plato','H1Red'),para('Cada captura corresponde a su propia terminal de VS Code. El mensaje de Tomcat y la linea Started confirman que el servicio inicio correctamente y se conecto a su base de datos.','BodyBlack'),screenshot('microservicio-cliente-iniciado.png','Cliente iniciado correctamente: Tomcat en el puerto 8081 y ClienteApplication iniciada.',maxw=490,maxh=275),screenshot('microservicio-plato-iniciado.png','Plato iniciado correctamente: Tomcat en el puerto 8082 y PlatoApplication iniciada.',maxw=490,maxh=275),PageBreak(),para('Ejecucion de microservicio - Pedido','H1Red'),para('Pedido inicia en una terminal independiente, conectado a bd_pedido y listo para recibir solicitudes en el puerto 8083.','BodyBlack'),screenshot('microservicio-pedido-iniciado.png','Pedido iniciado correctamente: Tomcat en el puerto 8083 y PedidoApplication iniciada.',maxw=490,maxh=275),PageBreak()]

# 7 - Postman summary
story += [para('Pruebas integrales - Postman','H1Red'),para('La coleccion corregida se ejecuto con el Runner de Postman Desktop. El resumen real indica 28 aserciones automatizadas y 28 aprobadas, sin fallos ni errores. Los casos cubren operaciones CRUD, validaciones de entrada, referencias remotas entre servicios y limpieza de datos temporales.','BodyBlack'),screenshot('postman-resumen-28-aprobadas.png','Resultado del Runner de Postman: All tests 28, Passed 28, Failed 0, Errors 0.',maxw=510,maxh=470),para('La carpeta postman/Evaluacion03_GestionPedidos.postman_collection.json contiene las solicitudes ejecutadas. Los casos negativos tambien se consideran aprobados cuando el servicio devuelve el codigo esperado (por ejemplo 400, 409, 422 o 404).','Small'),PageBreak()]

# 7-9 - screenshots of actual tests, grouped 2/page
postman_groups=[
 [('postman-clientes-platos.png','CRUD de clientes y primeras validaciones de correo/documento.'),('postman-platos-pedidos.png','CRUD de platos, controles de precio/codigo y creacion de pedido.')],
 [('postman-pedidos-validaciones.png','Consulta/actualizacion del pedido, referencias inexistentes y cantidad invalida.'),('postman-limpieza.png','Estado no permitido, eliminacion del pedido y limpieza del cliente de prueba.')],
 [('postman-borrado-platos.png','Limpieza de los dos platos temporales al finalizar la coleccion.')]
]
for idx,group in enumerate(postman_groups, start=1):
    story.append(para(f'Evidencias Postman - secuencia {idx}','H1Red'))
    story.append(para('Las imagenes siguientes son capturas directas del resultado de ejecucion de Postman. La etiqueta PASS confirma que cada respuesta coincide con el codigo HTTP esperado por el caso.','BodyBlack'))
    for name,caption in group:
        story.append(screenshot(name,caption,maxw=500,maxh=315 if len(group)==2 else 390))
    if idx < len(postman_groups): story.append(PageBreak())

# Individual technical learning and scope
story += [para('Cinco aprendizajes tecnicos - conclusiones individuales','H1Red'),para('1. Aprendi a separar responsabilidades: Cliente administra personas, Plato administra el catalogo y Pedido coordina la compra.','BodyBlack'),para('2. Comprendi que JPA modela relaciones dentro de una base; entre servicios independientes conviene intercambiar IDs y validar referencias por REST.','BodyBlack'),para('3. Aplique DTO y validaciones para controlar los datos de entrada y responder con codigos HTTP claros ante datos invalidos o duplicados.','BodyBlack'),para('4. Implemente el calculo de subtotales y total usando el precio vigente que entrega el servicio Plato.','BodyBlack'),para('5. Aprendi a separar perfiles dev y prod: en desarrollo Hibernate actualiza tablas y en produccion valida la estructura existente.','BodyBlack'),para('Dificultad encontrada y solucion','H2Red'),para('La dificultad tecnica fue mantener referencias consistentes cuando Cliente, Plato y Pedido guardan datos en bases separadas, sin claves foraneas entre ellas. La solucion fue guardar los IDs remotos y consultar los servicios Cliente y Plato antes de persistir el pedido; si la referencia no existe o el servicio no responde, Pedido devuelve un error HTTP explicito.','BodyBlack'),para('Sustentacion individual - guia de 5 a 8 minutos','H2Red'),para('Para la exposicion puedo dedicar cerca de 1 minuto al objetivo y estructura; 1 minuto a las responsabilidades de los servicios; 1 a 2 minutos a la persistencia y comunicacion REST; 2 minutos a mostrar casos validos e invalidos en Postman; y 1 minuto a mis aprendizajes y la dificultad resuelta. Debo demostrar el sistema en vivo y estar preparado para un cambio pequeno solicitado por el docente.','BodyBlack'),para('Correspondencia con la asignacion individual','H2Red'),para('El PDF Evaluacion 03 - D asigna a CASTILLO SUMIRE, JESUS JOSE el laboratorio individual N. 4: Pedidos de restaurante, con los microservicios Cliente, Plato y Pedido. Este informe corresponde a esa asignacion. Las referencias entre los servicios se representan mediante IDs; la configuracion de URLs se documenta en el README.','BodyBlack'),para('Ubicacion del proyecto','H2Red'),para('Semana12/Evaluacion 3/ dentro del repositorio Desarrollo-de-Aplicaciones-Web. Las evidencias fuente estan en evidencias/capturas y la coleccion ejecutada esta en postman/.','BodyBlack')]

# no page headers or footers, per requested format

doc.build(story)
print(OUT)

