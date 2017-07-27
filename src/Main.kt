import java.io.File
import java.io.PrintWriter
import java.util.*
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.charset.StandardCharsets
import java.io.FileWriter

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import java.lang.Integer.parseInt


/**
 * Created by james-clark-5 (Idios) on 12/06/17.
 * Graded-Reader-Builder
 * Description: a program to take simple text input and a vocabulary list to produce a graded reader book.
 */

// todo add table of contents to template, so code can be used to "fill it out"
//
//\lfoot{
//    x. 对她说 (\pinyin{dui4ta1shuo1}) said to her\\
//}
// and rfoot similarly

// todo: add support for figures
// add figures to tex (if line.contains(".png"))
//\begin{figure}[ht!]
//\centering
//\includegraphics[width=90mm]{exampleFigure.png}
//%	\caption{A simple caption \label{overflow}}
//\end{figure}


//TODO: Foreach[vocabHanzi] Scan through pages 1->end until first refererence is found. Repeat for all vocab. Note pages, change superscripts, add footers....

fun main(args: Array<String>) {
    // load input/output files

    val inputHeaderFilename: String = "res/inputHeader"
    val inputTitleFilename: String = "res/inputTitle"
    val inputStoryFilename: String = "res/inputStory"

    val inputVocabFilename: String = "res/inputVocab"
    val inputKeyNamesFilename: String = "res/inputKeyNames"

    val outputStoryFilename: String = "output/outputStory.tex"
    val outputMarkedUpStoryFilename: String = "output/outputStoryMarked.tex"
    val outputPDFFilename: String = "output/outputStory.pdf"

    // Create arrays to store vocab info
    var vocabArray: ArrayList<String> = ArrayList<String>()
    var vocabComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var keyNameArray: ArrayList<String> = ArrayList<String>()
    var keyNameComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()
    var pdfPageFirstSentences: ArrayList<String> = ArrayList<String>()
    var texLinesPDFPageFirstSentence: ArrayList<Int> = ArrayList<Int>()
    ;
    // create writer for output
    val outputStoryWriter = PrintWriter(outputStoryFilename, "UTF-8")

    // PREPARATION of vocab, key names, etc.
    vocabToArray(inputVocabFilename, vocabArray, vocabComponentArray) // split input text file into arrays of components
    vocabToArray(inputKeyNamesFilename, keyNameArray, keyNameComponentArray)
// TODO rename this function and the variables inside it, to generalise

    // create output from input files
    copyToTex(outputStoryWriter, inputHeaderFilename)
    copyToTex(outputStoryWriter, inputTitleFilename)
    copyToTex(outputStoryWriter, inputStoryFilename)
    outputStoryWriter.close() // close the outputStoryWriter for now (DEBUGGING)




    // add vocab page at end
    val outputStoryWriterRevisited = PrintWriter(FileWriter(outputStoryFilename, true))
    writeTexVocab(outputStoryWriterRevisited, inputVocabFilename, vocabComponentArray)

    // close writing file
    outputStoryWriterRevisited.append("\\end{document}") // end the TeX document
    outputStoryWriterRevisited.close()

    // generate pdf via xelatex (installed w/ TeXLive)
    xelatexToPDF(outputStoryFilename)

    // get pdf info
    readPDF(outputPDFFilename, vocabComponentArray, pdfPageFirstSentences)

    // recreate pdf w/ markup (and TODO: footers)

    getTexLineNumber(outputStoryFilename, pdfPageFirstSentences, texLinesPDFPageFirstSentence)
    println("sentences " + pdfPageFirstSentences + " at TeX lines " + texLinesPDFPageFirstSentence)

    // add markup
    addMarkup(vocabComponentArray, outputStoryFilename, "superscript")
    addMarkup(keyNameComponentArray, outputStoryFilename, "underline")

    // FOR ALL PAGES, FOR EACH VOCAB COMPONENT WHOSE PAGE NUMBER EQUALS PAGE NUMBER....

    xelatexToPDF(outputStoryFilename)
}

fun getTexLineNumber(outputStoryFilename: String, pdfPageFirstSentences: ArrayList<String>, texLinesPDFPageFirstSentence: ArrayList<Int>){

    println("pdfPageFirstSentences: " + pdfPageFirstSentences)
    pdfPageFirstSentences.forEachIndexed { index, pdfPageFirstSentence ->
        val inputFile: File = File(outputStoryFilename) // get file ready
        val scan: Scanner = Scanner(inputFile)
        var lineCount: Int = 0

        while (scan.hasNextLine()) {                              // TODO stop scanning when the first occurance is met
            val line: String = scan.nextLine()
            if (line.contains(pdfPageFirstSentence)) {
                texLinesPDFPageFirstSentence.add(lineCount+1)
            }
            lineCount+=1
        }
        scan.close()
    }
}

fun vocabToArray(inputFilename: String, inputArray: ArrayList<String>, inputComponentArray: ArrayList<ArrayList<String>>){
    val inputFile: File = File(inputFilename) // get file ready
    val scan: Scanner = Scanner(inputFile)
    var lineCount: Int = 0
    var tmpComponentArrayList:ArrayList<String> = ArrayList<String>()
    while(scan.hasNextLine()) {
        val line: String = scan.nextLine() // read all lines


        // split each entry into 3 components (e.g. Chinese, Pinyin, English)
        // get Chinese & Pinyin-English substrings
        var componentList: List<String> = line.split(" ")
        var zhPinyinSplitIndex: Int = line.indexOf("|")
        var chineseSplit: String = line.substring(0, zhPinyinSplitIndex)
        var pinyinEnglishSubstring: String = line.substring(zhPinyinSplitIndex+1, line.length)
        // get Pinyin and English substrings
        var pinyinEnglishSplitIndex: Int = pinyinEnglishSubstring.indexOf("|")
        var pinyinSplit: String = pinyinEnglishSubstring.substring(0, pinyinEnglishSplitIndex)
        var englishSplit: String = pinyinEnglishSubstring.substring(pinyinEnglishSplitIndex+1, pinyinEnglishSubstring.length)

        var ArrayListInitialiser: ArrayList<String> = ArrayList<String>(Collections.singletonList(""))

        // store the whole entry (to go directly into the footer)
        inputArray.add(chineseSplit + pinyinSplit + ": " + englishSplit)

        // store the individual Chinese and English components
        inputComponentArray.add(ArrayListInitialiser)
        inputComponentArray[lineCount].add(chineseSplit)
        inputComponentArray[lineCount].add(pinyinSplit)
        inputComponentArray[lineCount].add(englishSplit)
        inputComponentArray[lineCount].remove(inputComponentArray[lineCount][0]) // "uninitilaise" ArrayList empty entry

        lineCount+=1
    }
    scan.close()
}

fun copyToTex(outputStoryWriter: PrintWriter, inputFilename: String){
    val inputFile: File = File(inputFilename) // get file ready
    val scan: Scanner = Scanner(inputFile)

    while(scan.hasNextLine()) {
        val line: String = scan.nextLine() // read all lines
        if (line.contains("Chapter")) {   // add chapter markup if dealing with a chapter
            outputStoryWriter.println("\\clearpage")
            outputStoryWriter.println("{\\centering \\large")
            outputStoryWriter.println("{\\uline{" + line + "}}\\\\}")
        }
        else {     // else (for now) assume we have ordinary text
            outputStoryWriter.println(line)
        }
    }
    scan.close()
}

fun addVocabFootersOld(vocabComponentArray: ArrayList<ArrayList<String>>, outputStoryFilename: String, outputStoryWriter: PrintWriter ){
    // open the output file, with header and story
    val outputStoryFile: File = File(outputStoryFilename)
    val scan: Scanner = Scanner(outputStoryFile)
    // for each word in our vocab list, find the first entry and add a footer
    // n.b. add superscripts later
    vocabComponentArray.forEachIndexed { index, currentComponentArray ->
        while(scan.hasNextLine()) {
            val line: String = scan.nextLine()
            if (line.contains((vocabComponentArray[index][0]))) {
                // add footer text                  TODO figure out how to approach this
//                \lfoot{
//                    x. 对她说 (\pinyin{dui4ta1shuo1}) said to her\\
//                    x. 啊 (\pinyin{a1}) who knows..\\
//                    x. 聪明 (\pinyin{cong1ming}) intelligent\\
//                }
//

                outputStoryWriter.println("\\setstretch{1.5}")

                break
            }
        }
    }
}

// TODO FOR A GIVEN PAGE: COMPARE PDF-STRIPPED TEXT (1ST SENTENCE?) W/ TEX(THAT'S HAD MARKUPS REMOVED) TO GET LINE NUMBER IN .TEX FILE
// FOR ALL PAGES, FOR EACH VOCAB COMPONENT WHOSE PAGE NUMBER EQUALS PAGE NUMBER....

fun addVocabFooters(vocabComponentArray: ArrayList<ArrayList<String>>, outputStoryFilename: String, outputStoryWriter: PrintWriter ){
    // open the output file, with header and story
    val outputStoryFile: File = File(outputStoryFilename)
    val scan: Scanner = Scanner(outputStoryFile)
    // for each word in our vocab list, find the first entry and add a footer
    // n.b. add superscripts later
    vocabComponentArray.forEachIndexed { index, currentComponentArray ->
        while(scan.hasNextLine()) {
            val line: String = scan.nextLine()
            if (line.contains((vocabComponentArray[index][0]))) {
                // add footer text                  TODO figure out how to approach this
//                \lfoot{
//                    x. 对她说 (\pinyin{dui4ta1shuo1}) said to her\\
//                    x. 啊 (\pinyin{a1}) who knows..\\
//                    x. 聪明 (\pinyin{cong1ming}) intelligent\\
//                }
//

                outputStoryWriter.println("\\setstretch{1.5}")

                break
            }
        }
    }
}


fun addMarkup(inputArray: ArrayList<ArrayList<String>>, outputStoryFilename: String, markupType: String){
    // prepare to replace content in outputStoryFile
    val path = Paths.get(outputStoryFilename)
    val charset = StandardCharsets.UTF_8
    var content = String(Files.readAllBytes(path), charset)

    // replace add markup to existing words (via replacement)
    inputArray.forEachIndexed { index, inputArrayElement ->
        if (markupType=="underline"){
            content = content.replace(inputArrayElement[0].toRegex(), "\\\\uline{" + inputArrayElement[0] + "}")
        }
        else if (markupType=="superscript"){
            var firstVocabOccurance: Int = parseInt(inputArrayElement[3])-1 //-1 because of title page
            content = content.replace(inputArrayElement[0].toRegex(), inputArrayElement[0] + "\\\\textsuperscript{" + firstVocabOccurance + "." + (index+1) + "}")
        }
    }
    Files.write(path, content.toByteArray(charset))
}

fun writeTexVocab(outputStoryWriter: PrintWriter, inputVocabFilename: String, vocabComponentArray: ArrayList<ArrayList<String>>){
    // add page title, remove indenting
    outputStoryWriter.println("\\clearpage")
    outputStoryWriter.println("\\setlength{\\parindent}{0ex}")
    outputStoryWriter.println("\\centerline{Vocabulary}")

    // print all vocab entries to page
    vocabComponentArray.forEachIndexed { index, currentComponentArray ->
        outputStoryWriter.println("" + (index+1) + ". " + vocabComponentArray[index][0] + " " + "\\pinyin{" + vocabComponentArray[index][1]+ "}: " + vocabComponentArray[index][2] + "\\\\")
    }
}

fun xelatexToPDF (outputStoryFilename: String){
    val process = Runtime.getRuntime().exec("cmd /c start /wait buildPDF.sh")
    val exitVal = process.waitFor()
}



fun readPDF (PDFFilename: String, vocabComponentArray: ArrayList<ArrayList<String>>, pdfPageFirstSentences: ArrayList<String>){
    val PDFFile: File = File(PDFFilename)
    val documentPDF: PDDocument = PDDocument.load(PDFFile)
    val numberOfPages = documentPDF.getNumberOfPages()
    println("Number of pages: " + numberOfPages)

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
        while (pageCounter<numberOfPages) { // for each page
            val stripper = PDFTextStripper()
            stripper.startPage = pageCounter
            stripper.endPage = pageCounter
            pdfPageText = stripper.getText(documentPDF)

            var pdfPageTextLines: List<String> = pdfPageText.split("\r\n") //   \r   vs   \n   vs   \r\n    ..?
            println("pdfPageTextLines (first line on page " + pageCounter +"):" + pdfPageTextLines[0])

            pdfPageFirstSentences.add(pdfPageTextLines[0])
//            println("pdfPageFirstSentences: " + pdfPageFirstSentences)
            pageCounter +=1 // prepare to look at next page
        }
    }
    catch(e: Exception){    }

    documentPDF.close()
}


// TODO fun writeTexGrammar
