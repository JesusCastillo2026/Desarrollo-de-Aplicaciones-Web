from pathlib import Path
from textwrap import wrap

from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[2]
OUT = Path(__file__).resolve().parent / "evidencias"
OUT.mkdir(parents=True, exist_ok=True)

BG = "#111827"
PANEL = "#1f2937"
LINE = "#374151"
TEXT = "#e5e7eb"
MUTED = "#9ca3af"
CYAN = "#67e8f9"
GREEN = "#86efac"
ORANGE = "#fbbf24"
BLUE = "#93c5fd"

FONT_DIR = Path(r"C:\Windows\Fonts")
REGULAR = ImageFont.truetype(str(FONT_DIR / "consola.ttf"), 24)
BOLD = ImageFont.truetype(str(FONT_DIR / "consolab.ttf"), 24)
UI = ImageFont.truetype(str(FONT_DIR / "segoeui.ttf"), 23)
UI_BOLD = ImageFont.truetype(str(FONT_DIR / "segoeuib.ttf"), 26)
SMALL = ImageFont.truetype(str(FONT_DIR / "segoeui.ttf"), 17)


def wrapped_code(line, limit=118):
    stripped = line.rstrip("\n\r")
    if not stripped:
        return [""]
    return wrap(stripped, width=limit, subsequent_indent="    ", replace_whitespace=False,
                drop_whitespace=False, break_long_words=True) or [""]


def syntax_color(line):
    s = line.strip()
    if s.startswith("//") or s.startswith("*"):
        return GREEN
    if s.startswith("@"):
        return CYAN
    if any(token in s for token in ["return ", "throw ", "new ", "public ", "private ", "class ", "import "]):
        return BLUE
    if '"' in s or "'" in s:
        return ORANGE
    return TEXT


def code_image(filename, title, line_start, line_end, outname):
    source = ROOT / filename
    lines = source.read_text(encoding="utf-8-sig").splitlines()
    selected = lines[line_start - 1:line_end]
    expanded = []
    for idx, line in enumerate(selected, line_start):
        parts = wrapped_code(line)
        for part_no, part in enumerate(parts):
            expanded.append((idx if part_no == 0 else "", part, syntax_color(line)))
    line_h = 34
    height = 90 + line_h * len(expanded) + 25
    width = 1700
    im = Image.new("RGB", (width, height), BG)
    d = ImageDraw.Draw(im)
    d.rounded_rectangle((18, 16, width - 18, height - 16), radius=18, fill=PANEL, outline=LINE, width=2)
    d.ellipse((42, 38, 56, 52), fill="#fb7185")
    d.ellipse((68, 38, 82, 52), fill="#fbbf24")
    d.ellipse((94, 38, 108, 52), fill="#34d399")
    d.text((132, 31), f"{title}  |  {filename}  |  líneas {line_start}–{line_end}", font=UI_BOLD, fill=TEXT)
    y = 82
    for number, text, color in expanded:
        d.text((48, y), f"{number:>4}" if isinstance(number, int) else "    ·", font=REGULAR, fill=MUTED)
        d.text((122, y), text, font=REGULAR, fill=color)
        y += line_h
    im.save(OUT / outname, quality=94)


def read_tsv(filename):
    rows = []
    for line in (OUT / filename).read_text(encoding="utf-8-sig").splitlines():
        if line.strip():
            rows.append(line.split("\t"))
    return rows


def title_block(draw, title, subtitle, width=1700):
    draw.text((62, 42), title, font=UI_BOLD, fill="#173b6c")
    draw.text((62, 82), subtitle, font=SMALL, fill="#475569")
    draw.line((62, 116, width - 62, 116), fill=LINE, width=2)


def database_image():
    counts = read_tsv("conteo-tablas.tsv")[1:]
    sec = read_tsv("seguridad-auditoria.tsv")
    roles = sec[1:4]
    users_start = 5
    users = sec[users_start:users_start + 3]
    audit_start = users_start + len(users) + 1
    audit = sec[audit_start:]

    width, height = 1700, 1340
    im = Image.new("RGB", (width, height), "#f3f6fb")
    d = ImageDraw.Draw(im)
    d.rectangle((0, 0, width, 152), fill="#173b6c")
    d.text((62, 35), "EVIDENCIA DE BASE DE DATOS · XAMPP / MariaDB", font=UI_BOLD, fill="#ffffff")
    d.text((62, 88), "127.0.0.1:3306  |  hospital_db  |  MariaDB 10.4.32 · consulta directa", font=UI, fill="#dbeafe")
    d.text((62, 178), "Tablas y filas contadas con COUNT(*)", font=UI_BOLD, fill="#173b6c")

    x0, y0 = 62, 226
    colw = [690, 180]
    d.rounded_rectangle((x0, y0, x0 + sum(colw), y0 + 48), radius=8, fill="#dce8f7")
    d.text((x0 + 16, y0 + 11), "Tabla", font=BOLD, fill="#173b6c")
    d.text((x0 + colw[0] + 18, y0 + 11), "Filas", font=BOLD, fill="#173b6c")
    y = y0 + 48
    for table, count in counts:
        d.rectangle((x0, y, x0 + sum(colw), y + 38), fill="#ffffff" if (y // 38) % 2 else "#f6f9fd", outline="#cbd5e1")
        d.text((x0 + 16, y + 7), table, font=REGULAR, fill="#172033")
        d.text((x0 + colw[0] + 18, y + 7), count, font=REGULAR, fill="#172033")
        y += 38

    right_x = 1000
    d.text((right_x, 178), "Roles cargados", font=UI_BOLD, fill="#173b6c")
    y = 226
    for _, name, desc, active in roles:
        d.rounded_rectangle((right_x, y, 1638, y + 74), radius=10, fill="#ffffff", outline="#cbd5e1", width=2)
        d.text((right_x + 16, y + 9), name, font=BOLD, fill="#173b6c")
        d.text((right_x + 16, y + 40), "ACTIVO", font=SMALL, fill="#047857")
        y += 84

    d.text((right_x, 498), "Usuarios de laboratorio", font=UI_BOLD, fill="#173b6c")
    y = 546
    for _, username, role, active in users:
        d.rounded_rectangle((right_x, y, 1638, y + 62), radius=10, fill="#ffffff", outline="#cbd5e1", width=2)
        d.text((right_x + 16, y + 8), username, font=BOLD, fill="#172033")
        d.text((right_x + 16, y + 34), role + " · ACTIVO", font=SMALL, fill="#047857")
        y += 70

    d.text((right_x, 782), "Auditoría de prueba", font=UI_BOLD, fill="#173b6c")
    y = 830
    for row in audit:
        if len(row) < 6:
            continue
        _, user, when, action, entity, record_id = row[:6]
        d.rounded_rectangle((right_x, y, 1638, y + 72), radius=10, fill="#ffffff", outline="#cbd5e1", width=2)
        d.text((right_x + 16, y + 8), f"{action} · {entity} · id {record_id}", font=BOLD, fill="#172033")
        d.text((right_x + 16, y + 41), f"{when} · usuario {user}", font=SMALL, fill="#475569")
        y += 82
    d.text((62, 1160), "Datos extraídos de hospital_db después del arranque de la aplicación.", font=SMALL, fill="#475569")
    d.text((62, 1192), "La auditoría incluye dos altas de usuario y una actualización realizadas para comprobar AOP.", font=SMALL, fill="#475569")
    d.text((62, 1250), "Nota: la columna de filas se obtuvo mediante conteos exactos; varias tablas quirúrgicas aún no tienen registros.", font=SMALL, fill="#475569")
    im.save(OUT / "mysql-hospital-db.jpg", quality=95)


def foreign_keys_image():
    rows = read_tsv("claves-foraneas.tsv")[1:]
    width, height = 1700, 970
    im = Image.new("RGB", (width, height), "#f3f6fb")
    d = ImageDraw.Draw(im)
    title_block(d, "Relaciones verificadas en MySQL", "Lectura de information_schema.KEY_COLUMN_USAGE · hospital_db", width)
    x0, y0 = 62, 148
    widths = [500, 330, 500, 260]
    headers = ["Tabla", "Columna FK", "Tabla referenciada", "Clave PK"]
    d.rectangle((x0, y0, x0 + sum(widths), y0 + 48), fill="#dce8f7")
    x = x0
    for head, w in zip(headers, widths):
        d.text((x + 12, y0 + 12), head, font=BOLD, fill="#173b6c")
        x += w
    y = y0 + 48
    for row in rows:
        table, col, target, target_col = row
        vals = [table, col, target, target_col]
        d.rectangle((x0, y, x0 + sum(widths), y + 49), fill="#ffffff", outline="#cbd5e1")
        x = x0
        for val, w in zip(vals, widths):
            d.text((x + 12, y + 14), val, font=REGULAR, fill="#172033")
            x += w
        y += 49
    d.text((62, height - 54), f"Total comprobado en la base: {len(rows)} claves foráneas.", font=SMALL, fill="#475569")
    im.save(OUT / "mysql-foreign-keys.jpg", quality=95)


def access_matrix_image():
    rows = [
        ("Administrador", "GET /api/usuarios", "200", "Administración disponible"),
        ("Médico", "GET /api/programaciones", "200", "Puede consultar programación"),
        ("Médico", "GET /api/usuarios", "403", "Administración denegada"),
        ("Recepcionista", "GET /api/solicitudes", "200", "Puede gestionar solicitudes"),
        ("Recepcionista", "GET /api/programaciones", "403", "Programación denegada"),
        ("Administrador", "POST /api/usuarios (datos inválidos)", "400", "Validación centralizada en JSON"),
        ("Administrador", "GET /web/admin/usuarios", "200", "Pantalla administrativa disponible"),
        ("Administrador", "GET /web/admin/roles", "200", "Pantalla de roles disponible"),
        ("Administrador", "GET /web/admin/auditoria", "200", "Auditoría disponible"),
    ]
    width, height = 1700, 670
    im = Image.new("RGB", (width, height), "#f3f6fb")
    d = ImageDraw.Draw(im)
    title_block(d, "Comprobación de autenticación y permisos", "Solicitudes HTTP realizadas contra la aplicación local en localhost:8080", width)
    x0, y0 = 62, 150
    widths = [230, 470, 130, 650]
    headers = ["Perfil", "Recurso", "HTTP", "Resultado observado"]
    x = x0
    for head, w in zip(headers, widths):
        d.rectangle((x, y0, x + w, y0 + 48), fill="#dce8f7", outline="#cbd5e1")
        d.text((x + 12, y0 + 12), head, font=BOLD, fill="#173b6c")
        x += w
    y = y0 + 48
    for role, resource, status, result in rows:
        x = x0
        fill = "#ecfdf5" if status.startswith("2") else "#fff1f2"
        for val, w in zip([role, resource, status, result], widths):
            d.rectangle((x, y, x + w, y + 52), fill=fill, outline="#cbd5e1")
            color = "#047857" if status.startswith("2") else "#be123c"
            d.text((x + 12, y + 14), val, font=REGULAR, fill=color if val == status else "#172033")
            x += w
        y += 52
    im.save(OUT / "roles-http-checks.jpg", quality=95)


if __name__ == "__main__":
    code_image("src/main/java/com/hospital/quirofano/entity/Usuario.java", "Relación Usuario → Rol", 37, 59, "codigo-usuario-rol.jpg")
    code_image("src/main/java/com/hospital/quirofano/entity/Rol.java", "Relación inversa Rol → Usuarios", 32, 53, "codigo-rol-usuarios.jpg")
    code_image("src/main/java/com/hospital/quirofano/config/SecurityConfig.java", "Cadena de seguridad y rutas por rol", 16, 58, "codigo-security-config.jpg")
    code_image("src/main/java/com/hospital/quirofano/audit/AuditAspect.java", "Auditoría automática AOP", 16, 72, "codigo-audit-aspect.jpg")
    code_image("src/main/java/com/hospital/quirofano/service/UsuarioService.java", "CRUD, BCrypt y reglas de usuario", 34, 96, "codigo-usuario-service.jpg")
    code_image("src/main/java/com/hospital/quirofano/controller/AdministracionApiController.java", "API administrativa", 25, 62, "codigo-api-admin.jpg")
    code_image("src/main/java/com/hospital/quirofano/controller/ApiExceptionHandler.java", "Manejo global de errores REST", 17, 64, "codigo-api-exception-handler.jpg")
    code_image("src/main/java/com/hospital/quirofano/service/GestionQuirurgicaService.java", "Auditoría conectada al proceso quirúrgico", 64, 116, "codigo-auditoria-quirurgica.jpg")
    code_image("src/main/resources/application.properties", "Conexión a MySQL de XAMPP", 1, 11, "codigo-db-properties.jpg")
    database_image()
    foreign_keys_image()
    access_matrix_image()
    print(f"Evidencias generadas en {OUT}")
