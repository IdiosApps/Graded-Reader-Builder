import java.io.PrintWriter
import java.util.*

fun main(args: Array<String>) {

    // Process basically has 3 steps:
    // 1: Add input text to LaTeX file; convert LaTeX to pdf.
    // 2: Read pdf to get the first line on each page - this lets us locate this text in TeX and apply footers to the right page!
    // 3: Use page numbers to update LaTeX; convert LaTeX to pdf.

    val filenames = Filenames() // load defaults from class

    var vocabArray: ArrayList<String> = ArrayList<String>() // This is a list of all the input vocabulary
    var vocabComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>() // This an [array of [arrays containing input vocab split into parts]]
    var keyNameArray: ArrayList<String> = ArrayList<String>()
    var keyNameComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>() // This an [array of [arrays containing input key names split into parts]]
    var pdfPageFirstSentences: ArrayList<String> = ArrayList<String>()
    var texLinesPDFPageFirstSentence: ArrayList<Int> = ArrayList<Int>()
    var pdfNumberOfPages = 0 // todo tidy this up (return of pdf reading method?)

    val outputStoryTeXWriter = PrintWriter(filenames.outputStoryFilename, "UTF-8")

    VocabUtils.splitVocabIntoParts(filenames.inputVocabFilename, vocabArray, vocabComponentArray) // split vocab into e.g. [hanzi,pinyin,english]
    VocabUtils.splitVocabIntoParts(filenames.inputKeyNamesFilename, keyNameArray, keyNameComponentArray)

    TexUtils.copyToTex(outputStoryTeXWriter, filenames.inputHeaderFilename)
    TexUtils.copyToTex(outputStoryTeXWriter, filenames.inputTitleFilename)
    TexUtils.copyToTex(outputStoryTeXWriter, filenames.inputStoryFilename)

    WriteSummaryPages.writeVocabSection(outputStoryTeXWriter, filenames.inputVocabFilename, vocabComponentArray)
    // todo WriteSummaryPage.writeTexGrammar

    outputStoryTeXWriter.append("\\end{document}")
    outputStoryTeXWriter.close()

    PDFUtils.xelatexToPDF(filenames.outputStoryFilename)

    PDFUtils.readPDF(filenames.outputPDFFilename, vocabComponentArray, pdfPageFirstSentences)
    TexUtils.getTexLineNumber(filenames.outputStoryFilename, pdfPageFirstSentences, texLinesPDFPageFirstSentence)

    TeXStyling.addStyling(vocabComponentArray, filenames.outputStoryFilename, "superscript")
    TeXStyling.addStyling(keyNameComponentArray, filenames.outputStoryFilename, "underline")

    Footers.addVocabFooters(vocabComponentArray, filenames.outputStoryFilename, texLinesPDFPageFirstSentence)
    outputStoryTeXWriter.close()

    PDFUtils.xelatexToPDF(filenames.outputStoryFilename)
}