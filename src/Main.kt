import java.io.PrintWriter
import java.util.*
import java.io.FileWriter

fun main(args: Array<String>) {

    // Process basically has 3 steps:
    // 1: Add input text to LaTeX file; convert LaTeX to pdf.
    // 2: Read pdf to get page numbers.
    // 3: Use page numbers to update LaTeX; convert LaTeX to pdf.

    val filenames = Filenames() // load defaults from class

    var vocabArray: ArrayList<String> = ArrayList<String>() //todo use a custom class to store e.g. "vocab", making code a bit more adaptable.
    var vocabComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var keyNameArray: ArrayList<String> = ArrayList<String>()
    var keyNameComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var pdfPageFirstSentences: ArrayList<String> = ArrayList<String>()
    var texLinesPDFPageFirstSentence: ArrayList<Int> = ArrayList<Int>()
    var pdfNumberOfPages = 0 // todo tidy this up (return of pdf reading method?)

    val outputStoryWriter = PrintWriter(filenames.outputStoryFilename, "UTF-8")

    VocabUtils.vocabToArray(filenames.inputVocabFilename, vocabArray, vocabComponentArray) // split input text file into arrays of components
    VocabUtils.vocabToArray(filenames.inputKeyNamesFilename, keyNameArray, keyNameComponentArray)

    TexUtils.copyToTex(outputStoryWriter, filenames.inputHeaderFilename)
    TexUtils.copyToTex(outputStoryWriter, filenames.inputTitleFilename)
    TexUtils.copyToTex(outputStoryWriter, filenames.inputStoryFilename)
    outputStoryWriter.close() // close the outputStoryWriter for now (DEBUGGING)

    val outputStoryWriterRevisited = PrintWriter(FileWriter(filenames.outputStoryFilename, true)) //todo does this get closed?
    WriteSummaryPages.writeVocabPage(outputStoryWriterRevisited, filenames.inputVocabFilename, vocabComponentArray)
    // todo WriteSummaryPage.writeTexGrammar

    outputStoryWriterRevisited.append("\\end{document}")
    outputStoryWriterRevisited.close()

    PDFUtils.xelatexToPDF(filenames.outputStoryFilename)

    PDFUtils.readPDF(filenames.outputPDFFilename, vocabComponentArray, pdfPageFirstSentences, pdfNumberOfPages)
    TexUtils.getTexLineNumber(filenames.outputStoryFilename, pdfPageFirstSentences, texLinesPDFPageFirstSentence)

    Markup.addMarkup(vocabComponentArray, filenames.outputStoryFilename, "superscript")
    Markup.addMarkup(keyNameComponentArray, filenames.outputStoryFilename, "underline")

    Footers.addVocabFooters(vocabComponentArray, filenames.outputStoryFilename, texLinesPDFPageFirstSentence)

    PDFUtils.xelatexToPDF(filenames.outputStoryFilename)
}