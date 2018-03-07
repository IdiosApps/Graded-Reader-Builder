package PDFUtils

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.util.*

fun xelatexToPDF (outputStoryFilename: String){
        val process = Runtime.getRuntime().exec("cmd /c start /wait buildPDF.sh")
        val exitVal = process.waitFor()
    }

fun getNumberOfPDFPages (PDFFilename: String, pdfNumberOfPages: Int) : Int {
    val pdfFile = File(PDFFilename)
    val documentPDF: PDDocument = PDDocument.load(pdfFile)
    var pdfNumberOfPages = documentPDF.getNumberOfPages()
    return pdfNumberOfPages
}

fun readPDF (PDFFilename: String, vocabComponentArray: ArrayList<ArrayList<String>>, pdfPageFirstSentences: ArrayList<String>, pdfNumberOfPages: Int){
    val pdfFile = File(PDFFilename)
    val documentPDF: PDDocument = PDDocument.load(pdfFile)
    println("Number of pages: " + pdfNumberOfPages)

    // Find the first instance of each vocabulary word
    try {
        vocabComponentArray.forEachIndexed { index, currentVocabComponent ->
            var pageCounter = 1 // start at page 1 for each vocab Hanzi
            var pdfPageText = ""

            while(!pdfPageText.contains(currentVocabComponent[0])) {
                val stripper = PDFTextStripper()
                stripper.startPage = pageCounter
                stripper.endPage = pageCounter
                pdfPageText = stripper.getText(documentPDF)

                if (pdfPageText.contains(currentVocabComponent[0])){
                    currentVocabComponent.add(Integer.toString(pageCounter))
                }
                pageCounter +=1
            }
        }
    }
    catch(e: Exception){    }

    // Get the first sentence of each page, and save to array
    try {
        var pdfPageText = ""
        var pageCounter = 2 // start where the story starts (accounting for title page)
        while (pageCounter<pdfNumberOfPages) { // for each page
            val stripper = PDFTextStripper()
            stripper.startPage = pageCounter
            stripper.endPage = pageCounter
            pdfPageText = stripper.getText(documentPDF)

            var pdfPageTextLines: List<String> = pdfPageText.split("\r\n") //   \r   vs   \n   vs   \r\n    ..?

            pdfPageFirstSentences.add(pdfPageTextLines[0]) // todo improve efficiency; only need 1 (of maybe 20 lines)
            pageCounter +=1
        }
    }
    catch(e: Exception){}
    documentPDF.close()
}

