package PDFUtils

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.util.*

fun xelatexToPDF (outputStoryFilename: String){
        val process = Runtime.getRuntime().exec("cmd /c start /wait buildPDF.sh")
        val exitVal = process.waitFor()
    }

fun readPDF (PDFFilename: String, vocabComponentArray: ArrayList<ArrayList<String>>, pdfPageFirstSentences: ArrayList<String>, pdfNumberOfPages: Int){
    val PDFFile: File = File(PDFFilename)
    val documentPDF: PDDocument = PDDocument.load(PDFFile)
    val pdfNumberOfPages = documentPDF.getNumberOfPages()
    println("Number of pages: " + pdfNumberOfPages)

    // Find the first instance of each vocabulary word
    try {
        vocabComponentArray.forEachIndexed { index, currentVocabComponent ->
            var pageCounter: Int = 1 // start at page 1 for each vocab Hanzi
            var pdfPageText: String = ""
//          println("Hanzi to find: " + currentVocabHanzi[0])

            while(!pdfPageText.contains(currentVocabComponent[0])) {
                val stripper = PDFTextStripper()
                stripper.startPage = pageCounter
                stripper.endPage = pageCounter
                pdfPageText = stripper.getText(documentPDF)

//              println("pdfPageText: " + pdfPageText)

                if (pdfPageText.contains(currentVocabComponent[0])){
//                    println("Hanzi " + currentVocabComponent[0] + " - found in page " + pageCounter)
                    currentVocabComponent.add(Integer.toString(pageCounter))  // add the first occurrence of vocab to vocab element array
                }
                pageCounter +=1 // prepare to look at next page
            }
        }
    }
    catch(e: Exception){    }

    // Get the first sentence of each page, and save to array
    try {

        var pdfPageText: String = ""
        var pageCounter: Int = 2 // start at page 1
        while (pageCounter<pdfNumberOfPages) { // for each page
            val stripper = PDFTextStripper()
            stripper.startPage = pageCounter
            stripper.endPage = pageCounter
            pdfPageText = stripper.getText(documentPDF)

            var pdfPageTextLines: List<String> = pdfPageText.split("\r\n") //   \r   vs   \n   vs   \r\n    ..?
//            println("pdfPageTextLines (first line on page " + pageCounter +"):" + pdfPageTextLines[0])  // DEBUGGING

            pdfPageFirstSentences.add(pdfPageTextLines[0])
//            println("pdfPageFirstSentences: " + pdfPageFirstSentences)
            pageCounter +=1 // prepare to look at next page
        }
    }
    catch(e: Exception){    }

    documentPDF.close()
}

