from pathlib import Path
from xml.sax.saxutils import escape

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_LEFT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import mm
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Image, Table, TableStyle, XPreformatted,
    PageBreak, KeepTogether, HRFlowable
)

ROOT = Path(__file__).resolve().parents[2]
DOCS = Path(__file__).resolve().parent
EVID = DOCS / "evidencias"
OUTPUT = ROOT / "EVALUACION_02_Sistema_Gestion_Quirurgica.pdf"

FONT_DIR = Path(r"C:\Windows\Fonts")
pdfmetrics.registerFont(TTFont("Arial", str(FONT_DIR / "arial.ttf")))
pdfmetrics.registerFont(TTFont("Arial-Bold", str(FONT_DIR / "arialbd.ttf")))
pdfmetrics.registerFont(TTFont("Arial-Italic", str(FONT_DIR / "ariali.ttf")))
pdfmetrics.registerFont(TTFont("Consolas", str(FONT_DIR / "consola.ttf")))

NAVY = colors.HexColor("#c00000")
BLUE = colors.black
LIGHT = colors.Color(1, 1, 1, alpha=0)
INK = colors.black
MUTED = colors.black
GREEN = colors.HexColor("#047857")
RED = colors.HexColor("#be123c")
RULE = colors.black

PAGE_W, PAGE_H = A4
LEFT = RIGHT = 17 * mm
TOP = 17 * mm
BOTTOM = 17 * mm
CONTENT_W = PAGE_W - LEFT - RIGHT

styles = getSampleStyleSheet()
styles.add(ParagraphStyle(name="CoverTitle", fontName="Arial-Bold", fontSize=23,
                          leading=28, textColor=NAVY, alignment=TA_CENTER, spaceAfter=4))
styles.add(ParagraphStyle(name="CoverSub", fontName="Arial", fontSize=11,
                          leading=15, textColor=NAVY, alignment=TA_CENTER, spaceAfter=9))
styles.add(ParagraphStyle(name="H1x", fontName="Arial-Bold", fontSize=17,
                          leading=21, textColor=NAVY, spaceBefore=0, spaceAfter=8))
styles.add(ParagraphStyle(name="H2x", fontName="Arial-Bold", fontSize=12,
                          leading=15, textColor=NAVY, spaceBefore=8, spaceAfter=4))
styles.add(ParagraphStyle(name="Bodyx", fontName="Arial", fontSize=9.2,
                          leading=13.2, textColor=INK, spaceAfter=5))
styles.add(ParagraphStyle(name="Smallx", fontName="Arial", fontSize=8,
                          leading=10.5, textColor=INK, spaceAfter=3))
styles.add(ParagraphStyle(name="Tablex", fontName="Arial", fontSize=8.1,
                          leading=10.4, textColor=INK))
styles.add(ParagraphStyle(name="TableHeadx", fontName="Arial-Bold", fontSize=8.3,
                          leading=10.5, textColor=colors.white))
styles.add(ParagraphStyle(name="Captionx", fontName="Arial-Italic", fontSize=8,
                          leading=10, textColor=INK, alignment=TA_CENTER, spaceBefore=3, spaceAfter=7))
styles.add(ParagraphStyle(name="Calloutx", fontName="Arial", fontSize=8.7,
                          leading=12, textColor=INK, leftIndent=4, rightIndent=4))
styles.add(ParagraphStyle(name="WordCode", fontName="Arial", fontSize=6.8,
                          leading=8.0, textColor=INK,
                          leftIndent=0, rightIndent=0, spaceAfter=0))


def P(text, style="Bodyx"):
    return Paragraph(text, styles[style])


def heading(title, kicker=None):
    out = []
    if kicker:
        out.append(P(kicker.upper(), "Smallx"))
    out.append(P(title, "H1x"))
    out.append(HRFlowable(width="100%", thickness=1, color=RULE, spaceAfter=10))
    return out


def callout(text, background=LIGHT):
    table = Table([[P(text, "Calloutx")]], colWidths=[CONTENT_W])
    table.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, -1), colors.Color(1, 1, 1, alpha=0)),
        ("BOX", (0, 0), (-1, -1), 0.5, colors.black),
        ("LEFTPADDING", (0, 0), (-1, -1), 9),
        ("RIGHTPADDING", (0, 0), (-1, -1), 9),
        ("TOPPADDING", (0, 0), (-1, -1), 7),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 7),
    ]))
    return table


def evidence_image(name, max_width=CONTENT_W - 8 * mm):
    path = EVID / name
    image = Image(str(path))
    ratio = image.imageHeight / image.imageWidth
    image.drawWidth = min(max_width, CONTENT_W - 8 * mm)
    image.drawHeight = image.drawWidth * ratio
    image.hAlign = "CENTER"
    return image


def caption(text):
    return P(text, "Captionx")


def code_box(relative_path, first_line, last_line, label, font_size=6.15):
    """Plain source text inside a simple Word-like bordered table cell."""
    source = ROOT / relative_path
    all_lines = source.read_text(encoding="utf-8-sig").splitlines()
    excerpt = "\n".join(all_lines[first_line-1:last_line])
    code_style = ParagraphStyle(f"WordCode_{label}", parent=styles["WordCode"], fontSize=font_size,
                                leading=font_size * 1.22, wordWrap="CJK")
    box = Table([[XPreformatted(escape(excerpt), code_style)]], colWidths=[CONTENT_W - 4 * mm])
    box.setStyle(TableStyle([
        ("BACKGROUND", (0, 0), (-1, -1), colors.Color(1, 1, 1, alpha=0)),
        ("BOX", (0, 0), (-1, -1), 0.65, colors.HexColor("#808080")),
        ("LEFTPADDING", (0, 0), (-1, -1), 8),
        ("RIGHTPADDING", (0, 0), (-1, -1), 8),
        ("TOPPADDING", (0, 0), (-1, -1), 7),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 7),
    ]))
    return [P(f"<b>{escape(label)}</b>", "Smallx"), box]


def table(data, widths, header=True, font="Tablex"):
    converted = []
    for ri, row in enumerate(data):
        style = "TableHeadx" if header and ri == 0 else font
        converted.append([P(str(item), style) if not isinstance(item, Paragraph) else item for item in row])
    t = Table(converted, colWidths=widths, repeatRows=1 if header else 0, hAlign="LEFT")
    commands = [
        ("VALIGN", (0, 0), (-1, -1), "TOP"),
        ("GRID", (0, 0), (-1, -1), 0.45, RULE),
        ("LEFTPADDING", (0, 0), (-1, -1), 6),
        ("RIGHTPADDING", (0, 0), (-1, -1), 6),
        ("TOPPADDING", (0, 0), (-1, -1), 5),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
    ]
    if header:
        commands.append(("BACKGROUND", (0, 0), (-1, 0), colors.Color(1, 1, 1, alpha=0)))
        commands.append(("TEXTCOLOR", (0, 0), (-1, 0), colors.black))
        commands.append(("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.Color(1, 1, 1, alpha=0)]))
    else:
        commands.append(("ROWBACKGROUNDS", (0, 0), (-1, -1), [colors.Color(1, 1, 1, alpha=0)]))
    t.setStyle(TableStyle(commands))
    return t


def on_page(canvas, doc):
    # Se omiten encabezados y pies de página según indicación del usuario.
    return


doc = SimpleDocTemplate(str(OUTPUT), pagesize=A4, leftMargin=LEFT, rightMargin=RIGHT,
                        topMargin=TOP, bottomMargin=BOTTOM,
                        title="Evaluación 02 — Sistema de Gestión Quirúrgica",
                        author="Jesus Castillo Sumire",
                        subject="Seguridad por roles, relaciones JPA y auditoría AOP")
story = []

# Página 1: portada e introducción solicitada.
story += [Spacer(1, 3 * mm), P("EVALUACIÓN 02", "CoverTitle"),
          P("Sistema de Gestión Quirúrgica · Módulo de Quirófano", "CoverSub")]
identity = Table([
    [P("INTEGRANTE", "Smallx"), P("CURSO", "Smallx"), P("FECHA", "Smallx")],
    [P("Jesus Castillo Sumire", "Bodyx"), P("Desarrollo de Aplicaciones Web", "Bodyx"), P("18 de octubre de 2026", "Bodyx")],
], colWidths=[CONTENT_W * .36, CONTENT_W * .40, CONTENT_W * .24])
identity.setStyle(TableStyle([("BACKGROUND", (0, 0), (-1, -1), colors.Color(1, 1, 1, alpha=0)), ("BOX", (0, 0), (-1, -1), .5, colors.black),
                              ("VALIGN", (0, 0), (-1, -1), "TOP"), ("LEFTPADDING", (0, 0), (-1, -1), 8),
                              ("RIGHTPADDING", (0, 0), (-1, -1), 8), ("TOPPADDING", (0, 0), (-1, -1), 6),
                              ("BOTTOMPADDING", (0, 0), (-1, -1), 4)]))
story += [identity, Spacer(1, 5 * mm), P("Introducción y objetivo", "H2x")]
story.append(P("El objetivo de esta evaluación es ampliar el proyecto de quirófano de la Evaluación 1 con persistencia relacional comprobable, administración de usuarios y roles, control de acceso en páginas y API, manejo global de errores y auditoría automática de las operaciones. Se conservó el flujo quirúrgico construido previamente y se incorporaron las funciones de seguridad sobre la misma base <b>hospital_db</b> de XAMPP."))
story.append(P("Cómo se trabajó y cómo se logró", "H2x"))
story.append(P("Se revisaron primero las entidades, controladores y servicios existentes. Después se añadieron las relaciones JPA entre <b>Usuario</b> y <b>Rol</b>, las tablas nuevas y el usuario inicial; luego se configuraron Spring Security, BCrypt, las rutas autorizadas y un aspecto AOP que guarda cada operación correcta dentro de la misma transacción. El resultado se compiló y se inició conectado al MySQL local. La verificación directa confirmó 12 tablas, los tres roles solicitados y eventos reales en la tabla de auditoría."))
story.append(P("Herramientas y conexión", "H2x"))
story.append(P("Visual Studio Code para el código; Java 17, Spring Boot 4.0.8, Spring Data JPA/Hibernate, Spring Security, Spring AOP, Thymeleaf/Bootstrap y Maven para la aplicación; XAMPP MySQL/MariaDB (motor MariaDB 10.4.32) en <b>127.0.0.1:3306</b> con la base <b>hospital_db</b>; y Postman para repetir las solicitudes HTTP con Basic Auth. Apache de XAMPP habilita phpMyAdmin, mientras que la aplicación Java conecta directamente con el servicio de base de datos; Apache no es el servidor de la API."))
story.append(P("Flujo y procedimiento", "H2x"))
flow = Table([[P("Inicio de sesión → UserDetailsService → consulta de usuario y rol → BCrypt → autorización de ruta → Controller → Service → Repository/JPA → MySQL", "Calloutx")],
              [P("AOP intercepta la escritura correcta → guarda usuario, fecha/hora, acción, entidad e ID en auditoria_operaciones", "Calloutx")]], colWidths=[CONTENT_W])
flow.setStyle(TableStyle([("BACKGROUND", (0, 0), (-1, -1), colors.Color(1, 1, 1, alpha=0)), ("BOX", (0, 0), (-1, -1), .5, colors.black),
                          ("INNERGRID", (0, 0), (-1, -1), .4, RULE), ("LEFTPADDING", (0, 0), (-1, -1), 8),
                          ("RIGHTPADDING", (0, 0), (-1, -1), 8), ("TOPPADDING", (0, 0), (-1, -1), 6),
                          ("BOTTOMPADDING", (0, 0), (-1, -1), 6)]))
story.append(flow)
story.append(Spacer(1, 3 * mm))
story.append(P("Procedimiento realizado: iniciar MySQL en XAMPP → ejecutar la aplicación desde el proyecto abierto → dejar que Hibernate agregue las tablas → cargar roles y administrador de forma idempotente → ingresar con un perfil → verificar rutas permitidas/denegadas y revisar las filas de hospital_db. La aplicación conserva el esquema anterior mediante <font name='Arial-Bold'>ddl-auto=update</font>; no se borraron tablas de la Evaluación 1.", "Smallx"))

# Página 2: requerimientos y modelo JPA.
story.append(PageBreak())
story += heading("Alcance implementado y relaciones JPA", "01 · persistencia y arquitectura")
story.append(P("La segunda evaluación se implementó como ampliación del mismo proyecto. La tabla <b>usuarios</b> guarda el rol asignado con la clave foránea <b>rol_id</b>; la relación se modeló con <font name='Arial-Bold'>@ManyToOne</font> en Usuario y <font name='Arial-Bold'>@OneToMany(mappedBy = &quot;rol&quot;)</font> en Rol. En las entidades quirúrgicas existentes se mantuvieron las relaciones solicitud–paciente, médico e historia; la aprobación, evaluación, programación, quirófano y equipo conservan sus propias claves foráneas."))
story.append(P("La relación entre roles y usuarios es el requisito nuevo. Las relaciones quirúrgicas ya existían y se preservaron; Hibernate confirmó sus claves foráneas después del arranque.", "Smallx"))
story += code_box("src/main/java/com/hospital/quirofano/entity/Usuario.java", 37, 59, "Relación Usuario → Rol")
story.append(caption("Código nuevo: Usuario asocia cada cuenta con un Rol activo mediante rol_id."))
story.append(Spacer(1, 2 * mm))
story += code_box("src/main/java/com/hospital/quirofano/entity/Rol.java", 32, 53, "Relación inversa Rol → Usuarios")
story.append(caption("Código nuevo: Rol expone la relación inversa con sus usuarios."))

# Página 3: seguridad.
story.append(PageBreak())
story += heading("Autenticación y autorización por perfiles", "02 · seguridad")
story.append(P("El inicio de sesión web utiliza una cuenta persistida en MySQL; Postman puede autenticarse con Basic Auth. <b>BCryptPasswordEncoder</b> compara las contraseñas con el hash guardado. Se usa una consulta con <b>@EntityGraph</b> para cargar el rol junto con el usuario y evitar que la asociación diferida falle al autenticar."))
story.append(P("La cadena de seguridad protege en el servidor, además de ocultar opciones de interfaz. ADMINISTRADOR administra cuentas, roles y auditoría; MEDICO accede a evaluación, aprobación y programación; RECEPCIONISTA registra y consulta solicitudes. Las rutas de gestión de usuarios y roles quedan restringidas al administrador."))
story += code_box("src/main/java/com/hospital/quirofano/config/SecurityConfig.java", 16, 58, "Cadena de seguridad y rutas por rol", 5.7)
story.append(caption("Código nuevo: SecurityFilterChain, perfiles, ingreso con formulario/Basic Auth y carga del rol."))
story.append(callout("Administrador local inicial: usuario <b>admin</b> y contraseña <b>Admin123!</b>. El código la cifra con BCrypt antes de almacenarla. Es una credencial de laboratorio; debe cambiarse antes de cualquier despliegue real."))

# Página 4: AOP.
story.append(PageBreak())
story += heading("Auditoría transversal con AOP", "03 · trazabilidad")
story.append(P("La anotación <b>@AuditOperation</b> identifica acción y entidad en los métodos que escriben. <b>AuditAspect</b> se ejecuta después de un resultado exitoso, obtiene el usuario autenticado, toma el ID de la entidad devuelta o de los argumentos y envía el evento a AuditoriaService. La auditoría usa propagación obligatoria: si la operación principal falla, no queda un evento engañoso; cuando tiene éxito, el evento se guarda en la misma transacción."))
story += code_box("src/main/java/com/hospital/quirofano/audit/AuditAspect.java", 16, 72, "Auditoría automática AOP", 5.75)
story.append(caption("Código nuevo: aspecto que registra usuario, acción, entidad, identificador y método ejecutado."))
story.append(P("Se auditan altas, modificaciones, cambios de estado y cancelaciones de solicitudes; evaluación y decisión clínica; programación, reprogramación y cancelación; mantenimiento de quirófanos; usuarios y roles. La vista administrativa muestra hasta 200 eventos recientes. La fecha se persiste en hora local de Lima mediante configuración de zona horaria JDBC."))

# Página 5: operaciones del sistema quirúrgico.
story.append(PageBreak())
story += heading("Auditoría conectada al flujo de quirófano", "04 · operaciones existentes")
story.append(P("La auditoría se integra en los servicios que ya ejecutan las reglas de negocio. Por ejemplo, al registrar una evaluación, aprobar una solicitud o programar una cirugía, el servicio conserva las validaciones previas y AOP añade la fila correspondiente en <b>auditoria_operaciones</b>. No se reemplazaron las reglas de autorización clínica ni de disponibilidad de salas."))
story += code_box("src/main/java/com/hospital/quirofano/service/GestionQuirurgicaService.java", 64, 116, "Auditoría conectada al proceso quirúrgico", 5.8)
story.append(caption("Código del servicio quirúrgico: operaciones con transacción y anotación de auditoría."))
story.append(callout("El log registra el nombre del usuario como texto y no como FK. Así queda trazabilidad aun cuando una cuenta se desactive; el campo entidad + registro_id identifica la fila afectada."))

# Página 6: CRUD y pantallas.
story.append(PageBreak())
story += heading("Administración de usuarios y roles", "05 · CRUD e interfaz")
story.append(P("Se añadieron servicios y controladores REST/web para crear, listar, editar y activar/desactivar usuarios y roles. Las claves se actualizan solo cuando se proporciona una nueva contraseña; las respuestas REST usan DTO y no exponen <b>password_hash</b>. Se impide desactivar el último administrador activo y desactivar un rol que todavía tenga usuarios activos."))
story += code_box("src/main/java/com/hospital/quirofano/service/UsuarioService.java", 34, 96, "CRUD, BCrypt y reglas de usuario", 5.55)
story.append(caption("Código nuevo: validaciones, unicidad, asignación de rol, BCrypt y activación de cuenta."))
story.append(P("Las páginas nuevas son <b>/login</b>, <b>/web/admin/usuarios</b>, <b>/web/admin/roles</b> y <b>/web/admin/auditoria</b>. El ingreso de un administrador abre la administración de usuarios; médicos y recepción van al panel quirúrgico. La interfaz muestra opciones según el perfil, mientras SecurityFilterChain aplica la restricción real en el backend."))

# Página 7: API.
story.append(PageBreak())
story += heading("Endpoints administrativos y API", "06 · controladores")
story += code_box("src/main/java/com/hospital/quirofano/controller/AdministracionApiController.java", 25, 62, "API administrativa", 5.9)
story.append(caption("Código nuevo: endpoints administrativos con DTOs para no serializar entidades ni contraseñas."))
api_rows = [
    ["Método", "Ruta", "Acción"],
    ["GET / POST", "/api/usuarios", "Listar / crear cuenta"],
    ["PUT", "/api/usuarios/{id}", "Editar datos"],
    ["PUT", "/api/usuarios/{id}/estado", "Activar / desactivar"],
    ["GET / POST", "/api/roles", "Listar / crear rol"],
    ["PUT", "/api/roles/{id}", "Editar rol"],
    ["PUT", "/api/roles/{id}/estado", "Activar / desactivar"],
    ["GET", "/api/auditoria", "Ver últimos 200 eventos"],
]
story.append(table(api_rows, [28 * mm, 74 * mm, CONTENT_W - 102 * mm]))
story.append(Spacer(1, 3 * mm))
story.append(P("Los endpoints existentes de solicitudes, evaluación, decisiones, quirófanos y programación continúan disponibles bajo sus rutas anteriores, con permisos asociados al perfil. El SQL aditivo de referencia se encuentra en <b>database/evaluacion-02-seguridad-auditoria.sql</b>; el esquema en ejecución lo actualiza Hibernate."))

# Página 8: errores globales y validaciones.
story.append(PageBreak())
story += heading("Validación y manejo global de errores REST", "07 · respuestas consistentes")
story.append(P("Se centralizaron las excepciones de los controladores REST en un único advice. Así la API entrega errores en JSON con fecha/hora, estado HTTP, mensaje y ruta, en vez de repetir respuestas distintas en cada controlador. Se distinguen datos inválidos, JSON mal formado, referencias inexistentes, conflictos de integridad, falta de permisos y errores inesperados."))
story += code_box("src/main/java/com/hospital/quirofano/controller/ApiExceptionHandler.java", 17, 64, "Manejo global de errores REST", 5.7)
story.append(caption("Código nuevo: manejador global REST con estados HTTP y mensajes seguros."))
story.append(callout("Códigos manejados: 400 para validación/formato; 401 para credenciales no válidas; 403 para rol sin permiso; 404 para recursos no encontrados; 409 para duplicados o restricciones; 500 para errores inesperados. El mensaje de error interno no expone datos técnicos al cliente."))

# Página 9: evidencia MySQL.
story.append(PageBreak())
story += heading("Base de datos hospital_db en XAMPP", "08 · evidencia de MySQL")
story.append(P("La siguiente imagen se generó a partir de consultas directas a MariaDB de XAMPP en 127.0.0.1:3306 (conteos exactos con COUNT(*), roles, usuarios y eventos). XAMPP presenta el servicio como MySQL en su panel; el motor identificado por SELECT VERSION() es MariaDB 10.4.32."))
story.append(evidence_image("mysql-hospital-db.jpg", CONTENT_W - 6 * mm))
story.append(caption("Evidencia de datos reales consultados en hospital_db; las contraseñas no se consultaron ni se mostraron."))
story.append(P("El proyecto conserva 15 pacientes, 5 médicos y 15 historias clínicas; en la consulta de esta entrega había 16 solicitudes. Se añadieron las tablas de seguridad (roles, usuarios y auditoria_operaciones) y las tablas quirúrgicas complementarias que el proyecto ya mapeaba. Las tablas de evaluación, aprobación, quirófano, programación y equipo aún aparecen sin filas porque no se creó una cirugía de demostración."))

# Página 10: claves foráneas.
story.append(PageBreak())
story += heading("Claves foráneas verificadas", "09 · integridad referencial")
story.append(P("La consulta a information_schema.KEY_COLUMN_USAGE devolvió 13 claves foráneas en hospital_db. La imagen presenta los pares tabla/columna y tabla/clave primaria a los que apuntan. El nuevo vínculo del requisito de usuarios aparece como <b>usuarios.rol_id → roles.id_rol</b>."))
story.append(evidence_image("mysql-foreign-keys.jpg", CONTENT_W - 6 * mm))
story.append(caption("Consulta directa de relaciones del esquema después de la actualización Hibernate."))
story.append(P("Las relaciones quirúrgicas evitan referencias inexistentes en solicitudes, evaluaciones, aprobaciones, programación y equipo. La relación de auditoría con el usuario se mantiene por su nombre y no como FK, para que el registro histórico no dependa de la vida de la cuenta."))

# Página 11: diagrama Mermaid.
story.append(PageBreak())
story += heading("Diagrama final de relaciones", "10 · modelo relacional")
story.append(P("El diagrama se definió en Mermaid como <b>erDiagram</b> y se incluyó también su fuente en <b>docs/evaluacion-02/modelo-relacional.mmd</b>. Representa las 13 relaciones consultadas en MySQL; AUDITORIA_OPERACIONES aparece sin línea a USUARIOS porque guarda el nombre del actor como texto."))
diagram = Image(str(DOCS / "diagrama-relacional.jpg"))
diagram.drawWidth = CONTENT_W
diagram.drawHeight = CONTENT_W * diagram.imageHeight / diagram.imageWidth
diagram.hAlign = "CENTER"
story.append(diagram)
story.append(caption("Modelo de entidades JPA y claves foráneas comprobadas en la base de datos local."))
story.append(P("Flujo principal conservado: paciente/médico/historia → solicitud quirúrgica → evaluación preoperatoria y aprobación → programación en quirófano con profesionales → integrantes del equipo. El flujo de seguridad enlaza roles con usuarios y deja cada operación en la bitácora."))

# Página 12: comprobaciones realizadas y conclusión final.
story.append(PageBreak())
story += heading("Comprobaciones y conclusión", "11 · resultado")
story.append(P("Después de compilar e iniciar el proyecto se verificaron credenciales, vistas administrativas, serialización segura y autorización efectiva por perfil. La tabla resume respuestas observadas en solicitudes HTTP locales; se pueden repetir en Postman con Authorization → Basic Auth."))
story.append(evidence_image("roles-http-checks.jpg", CONTENT_W - 6 * mm))
story.append(caption("Comprobación local de permisos: rutas aceptadas responden 200 y rutas sin permiso responden 403."))
story.append(P("Cuentas locales para repetir los ejemplos", "H2x"))
creds = [
    ["Usuario", "Rol", "Contraseña local de demostración"],
    ["admin", "ADMINISTRADOR", "Admin123! (inicial)"],
    ["medico.demo", "MEDICO", "MedicoDemo2026!"],
    ["recepcion.demo", "RECEPCIONISTA", "RecepcionDemo2026!"],
]
story.append(table(creds, [40 * mm, 42 * mm, CONTENT_W - 82 * mm]))
story.append(Spacer(1, 3 * mm))
story.append(callout("Estas cuentas se dejaron activas en la base local para que se pueda reproducir la matriz de permisos. No reutilizar estas claves fuera del laboratorio; el administrador puede cambiarlas desde el panel."))
story.append(P("Conclusión", "H2x"))
story.append(P("Con esta evaluación completé una ampliación importante del sistema quirúrgico. Ahora las cuentas se relacionan con roles mediante JPA, las contraseñas se guardan con BCrypt y el backend valida qué puede hacer cada perfil. También quedó una bitácora transversal que muestra quién ejecutó la operación, qué registro afectó y cuándo ocurrió, sin llenar cada controlador con código repetido."))
story.append(P("La integración se realizó sobre la misma base hospital_db y mantuvo las tablas y relaciones que ya existían. La aplicación compiló y arrancó conectada a MySQL de XAMPP; las tablas, los roles, usuarios y claves foráneas se verificaron directamente, y las rutas protegidas respondieron según los permisos esperados. De esta manera la Evaluación 2 suma seguridad y trazabilidad al flujo de solicitud, evaluación y programación desarrollado en la Evaluación 1."))

doc.build(story, onFirstPage=on_page, onLaterPages=on_page)
print(f"PDF generado: {OUTPUT}")
