import java.io.PrintWriter
import java.util.*

fun main(args: Array<String>) {

    // Process basically has 3 steps:
    // 1: Add input text to LaTeX file; convert LaTeX to pdf.
    // 2: Read pdf to get the first line on each page - this lets us locate this text in TeX and apply footers to the right page!
    // 3: Use page numbers to update LaTeX; convert LaTeX to pdf.

    // TODO add multi-language support (could be a bit in the case that both L2 and L1 are used in the story.
    // If only L2 is used in the story (or if e.g. \arabictext{} doesn't get upset by e.g. some English)...
    // + then the story can simply be wrapped in \arabictext{}.
    // otherwise, solve this problem at the end of the text (before final pdf creation) by opening and closing languages appropriately.

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
    PDFUtils.readPDF(filenames.outputPDFFilename, vocabComponentArray, pdfPageLastSentences,pdfNumberOfPages)
    TexUtils.getTexLineNumbers(filenames.outputStoryFilename, pdfPageLastSentences, texLinesPDFPageLastSentences, texLinesLastSentenceIndex)

    TeXStyling.addStyling(vocabComponentArray, filenames.outputStoryFilename, "superscript")
    TeXStyling.addStyling(keyNameComponentArray, filenames.outputStoryFilename, "underline")

    FooterUtils.addVocabFooters(vocabComponentArray, filenames.outputStoryFilename, texLinesPDFPageLastSentences, languageUsed,pdfNumberOfPages, texLinesLastSentenceIndex,pdfPageLastSentences)
    outputStoryTeXWriter.close()

    PDFUtils.xelatexToPDF(filenames.outputStoryFilename)
}