from pathlib import Path
from PIL import Image, ImageDraw

files = sorted(Path(__file__).resolve().parent.joinpath("evidencias/rendered").glob("page-*.png"))
cards = []
for file in files:
    im = Image.open(file).convert("RGB")
    im.thumbnail((340, 480))
    card = Image.new("RGB", (360, 520), "white")
    card.paste(im, ((360 - im.width) // 2, 25))
    ImageDraw.Draw(card).text((12, 5), file.name, fill="black")
    cards.append(card)
sheet = Image.new("RGB", (360 * 4, 520 * ((len(cards) + 3) // 4)), "#bbbbbb")
for index, card in enumerate(cards):
    sheet.paste(card, ((index % 4) * 360, (index // 4) * 520))
sheet.save(Path(__file__).resolve().parent / "evidencias/contact-sheet.jpg", quality=90)
