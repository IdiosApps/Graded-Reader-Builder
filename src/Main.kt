import java.io.PrintWriter
import java.util.*
import java.io.FileWriter

fun main(args: Array<String>) {

    // Process basically has 3 steps:
    // 1: Add input text to LaTeX file; convert LaTeX to pdf.
    // 2: Read pdf to get page numbers.
    // 3: Use page numbers to update LaTeX; convert LaTeX to pdf.

    val inputHeaderFilename: String = "res/inputHeader"
    val inputTitleFilename: String = "res/inputTitle"
    val inputStoryFilename: String = "res/inputStory"

    val inputVocabFilename: String = "res/inputVocab"
    val inputKeyNamesFilename: String = "res/inputKeyNames"

    val outputStoryFilename: String = "output/outputStory.tex"
    val outputMarkedUpStoryFilename: String = "output/outputStoryMarked.tex"
    val outputPDFFilename: String = "output/outputStory.pdf"

    var vocabArray: ArrayList<String> = ArrayList<String>()
    var vocabComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var keyNameArray: ArrayList<String> = ArrayList<String>()
    var keyNameComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var pdfPageFirstSentences: ArrayList<String> = ArrayList<String>()
    var texLinesPDFPageFirstSentence: ArrayList<Int> = ArrayList<Int>()
    var pdfNumberOfPages = 0
    var rightFooter = StringBuilder("\\rfoot{ ")// .append(c).append(d) // .toString()
    var leftFooter = StringBuilder("\\lfoot{ ")// .append(c).append(d) // .toString()

    val outputStoryWriter = PrintWriter(outputStoryFilename, "UTF-8")

    VocabUtils.vocabToArray(inputVocabFilename, vocabArray, vocabComponentArray) // split input text file into arrays of components
    VocabUtils.vocabToArray(inputKeyNamesFilename, keyNameArray, keyNameComponentArray)

    TexUtils.copyToTex(outputStoryWriter, inputHeaderFilename)
    TexUtils.copyToTex(outputStoryWriter, inputTitleFilename)
    TexUtils.copyToTex(outputStoryWriter, inputStoryFilename)
    outputStoryWriter.close() // close the outputStoryWriter for now (DEBUGGING)

    val outputStoryWriterRevisited = PrintWriter(FileWriter(outputStoryFilename, true))
    WriteSummaryPages.writeVocabPage(outputStoryWriterRevisited, inputVocabFilename, vocabComponentArray)
    // todo WriteSummaryPage.writeTexGrammar

    outputStoryWriterRevisited.append("\\end{document}")
    outputStoryWriterRevisited.close()

    PDFUtils.xelatexToPDF(outputStoryFilename)

    PDFUtils.readPDF(outputPDFFilename, vocabComponentArray, pdfPageFirstSentences, pdfNumberOfPages)
    TexUtils.getTexLineNumber(outputStoryFilename, pdfPageFirstSentences, texLinesPDFPageFirstSentence)

    Markup.addMarkup(vocabComponentArray, outputStoryFilename, "superscript")
    Markup.addMarkup(keyNameComponentArray, outputStoryFilename, "underline")

    VocabUtils.getVocabIndicies(vocabComponentArray)

    Footers.addVocabFooters(vocabComponentArray, outputStoryFilename, outputStoryWriter, texLinesPDFPageFirstSentence, pdfNumberOfPages,leftFooter,rightFooter)

    PDFUtils.xelatexToPDF(outputStoryFilename)
}