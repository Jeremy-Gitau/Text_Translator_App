from translate import Translator

def main(text, source, destination):
    translator = Translator(from_lang = source, to_lang = destination)
    translation = translator.translate(text)

    return translation
