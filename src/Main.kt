import java.io.PrintWriter

     // Process is:
     // input    ->   tex    ->    pdf    ->    tex     ->    tex     -> pdf
     //                                     + styling     + footers     final

fun main(args: Array<String>) {

    var languageUsed = "mandarin"

    val filenames = Filenames() // load defaults from class
    var pdfNumberOfPages = 0

    var vocabArray: ArrayList<String> = ArrayList() // This is a list of all the input vocabulary
    var vocabComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>() // This an [array of [arrays containing input vocab split into parts]]
    var keyNameArray: ArrayList<String> = ArrayList()
    var keyNameComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>() // This an [array of [arrays containing input key names split into parts]]
    var pdfPageLastSentences: ArrayList<String> = ArrayList()
    var texLinesPDFPageLastSentences: ArrayList<Int> = ArrayList()
    var texLinesLastSentenceIndex: ArrayList<Int> = ArrayList()

    val outputStoryTeXWriter = PrintWriter(filenames.outputStoryFilename, "UTF-8")

    VocabUtils.splitVocabIntoParts(filenames.inputVocabFilename, vocabArray, vocabComponentArray)
    VocabUtils.splitVocabIntoParts(filenames.inputKeyNamesFilename, keyNameArray, keyNameComponentArray)

    TexUtils.copyToTex(outputStoryTeXWriter, filenames.inputHeaderFilename)
    TexUtils.copyToTex(outputStoryTeXWriter, filenames.inputTitleFilename)
    TexUtils.copyToTex(outputStoryTeXWriter, filenames.inputStoryFilename)

    WriteSummaryPages.writeVocabSection(outputStoryTeXWriter, filenames.inputVocabFilename, vocabComponentArray)
    // todo WriteSummaryPage.writeTexGrammar

    outputStoryTeXWriter.append("\\end{document}")
    outputStoryTeXWriter.close()

    PDFUtils.xelatexToPDF(filenames.outputStoryFilename)

    pdfNumberOfPages = PDFUtils.getNumberOfPDFPages(filenames.outputPDFFilename, pdfNumberOfPages)
    PDFUtils.readPDF(filenames.outputPDFFilename, vocabComponentArray, pdfPageLastSentences, pdfNumberOfPages)
    TexUtils.getTexLineNumbers(filenames.outputStoryFilename, pdfPageLastSentences, texLinesPDFPageLastSentences, texLinesLastSentenceIndex)

    TeXStyling.addStyling(vocabComponentArray, filenames.outputStoryFilename, "superscript")
    TeXStyling.addStyling(keyNameComponentArray, filenames.outputStoryFilename, "underline")

    FooterUtils.addVocabFooters(vocabComponentArray, filenames.outputStoryFilename, texLinesPDFPageLastSentences, languageUsed, pdfNumberOfPages, texLinesLastSentenceIndex, pdfPageLastSentences)
    outputStoryTeXWriter.close()

    PDFUtils.xelatexToPDF(filenames.outputStoryFilename)
}