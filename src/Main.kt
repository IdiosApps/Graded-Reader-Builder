import java.io.File
import java.io.PrintWriter
import java.util.*
import java.nio.file.Files
import java.nio.charset.Charset
import java.nio.file.Paths
import java.nio.charset.StandardCharsets
import kotlin.text.Charsets
import java.io.FileWriter







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


fun main(args: Array<String>) {
    // load input/output files
    val inputStoryFilename: String = "res/inputStory"
    val inputVocabFilename: String = "res/inputVocab"
    val inputHeaderFilename: String = "res/inputHeader"
    val outputStoryFilename: String = "res/outputStorytest.tex"

    // Create arrays to store vocab info
    var vocabArray: ArrayList<String> = ArrayList<String>()
    var vocabComponentArray: ArrayList<ArrayList<String>> = ArrayList<ArrayList<String>>()

    // create writer for output
    val outputStoryWriter = PrintWriter(outputStoryFilename, "UTF-8")

    // PREPARATION of vocab
    vocabToArray(inputVocabFilename, vocabArray, vocabComponentArray) // take the vocab text and split into components

    // create output from input files
    writeTexHeader(outputStoryWriter, inputHeaderFilename)
    writeTexStory(outputStoryWriter, inputStoryFilename)
    outputStoryWriter.close() // close the outputStoryWriter for now (DEBUGGING)

    // addVocabFooters()
    addVocabSubscripts(vocabComponentArray, outputStoryFilename)

    // add vocab page at end
    val outputStoryWriterRevisited = PrintWriter(FileWriter(outputStoryFilename, true))
    writeTexVocab(outputStoryWriterRevisited, inputVocabFilename, vocabComponentArray)
    
    // close writing file
    outputStoryWriterRevisited.append("\\end{document}") // end the TeX document
    outputStoryWriterRevisited.close()
}

fun vocabToArray(inputVocabFilename: String, vocabArray: ArrayList<String>, vocabComponentArray: ArrayList<ArrayList<String>>){
    val inputVocabFile: File = File(inputVocabFilename) // get file ready
    val scan: Scanner = Scanner(inputVocabFile)
    var vocabLineCount: Int = 0
    var tmpComponentArrayList:ArrayList<String> = ArrayList<String>()
    while(scan.hasNextLine()) {
        val line: String = scan.nextLine() // read all lines


        // split each entry into 3 components: Chinese, Pinyin, English
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
        vocabArray.add(chineseSplit + pinyinSplit + ": " + englishSplit)

        // store the individual Chinese and English components
        vocabComponentArray.add(ArrayListInitialiser)
        vocabComponentArray[vocabLineCount].add(chineseSplit)
        vocabComponentArray[vocabLineCount].add(pinyinSplit)
        vocabComponentArray[vocabLineCount].add(englishSplit)
        vocabComponentArray[vocabLineCount].remove(vocabComponentArray[vocabLineCount][0]) // "uninitilaise" ArrayList empty entry

        vocabLineCount+=1
    }
    scan.close()
    println("vocabArray: " + vocabArray)
    println("vocabComponentArray: " + vocabComponentArray)
}

fun writeTexHeader(outputStoryWriter: PrintWriter, inputHeaderFilename: String){
    val inputHeaderFile: File = File(inputHeaderFilename) // get file ready
    val scan: Scanner = Scanner(inputHeaderFile)

    while(scan.hasNextLine()) {
        val line: String = scan.nextLine() // read all lines
        outputStoryWriter.println(line)  // write all header lines to output file
    }
    scan.close()
}

fun writeTexStory(outputStoryWriter: PrintWriter, inputStoryFilename: String){
    val inputStoryFile: File = File(inputStoryFilename) // get file ready
    val scan: Scanner = Scanner(inputStoryFile)

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



    // TODO keep track of the which lines have page ends at
    //     this would let us scan between these lines, i.e. on a page-by-page basis, and would give us a lot of freedom
    // 22 lines per page with OR without a chapter
    //
}

fun addVocabFooters(vocabComponentArray: ArrayList<ArrayList<String>>, outputStoryFilename: String, outputStoryWriter: PrintWriter ){
    // open the output file, with header and story
    val outputStoryFile: File = File(outputStoryFilename)
    val scan: Scanner = Scanner(outputStoryFile)
    // for each word in our vocab list, find the first entry and add a footer
    // n.b. add superscripts later
    vocabComponentArray.forEachIndexed { index, currentSubStringWordType ->
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

fun addVocabSubscripts(vocabComponentArray: ArrayList<ArrayList<String>>, outputStoryFilename: String){
    // open the output file, with header and story
//    val outputStoryFile: File = File(outputStoryFilename)
//    val scan: Scanner = Scanner(outputStoryFile)

    // prepare to replace content in outputStoryFile
    val path = Paths.get(outputStoryFilename)
    val charset = StandardCharsets.UTF_8
    var content = String(Files.readAllBytes(path), charset)

//    val contentNew = readFile("test.txt", StandardCharsets.UTF_8)
//
//    var contentbest: String = Files.readAllBytes(Paths.get("res/outputStorytest.tex"));
//
    println(File("res\\outputStorytest.tex").readText(charset = Charsets.UTF_8))


//    val text = String(Files.toString(File(path), Charsets.UTF_8))
//    val contentNewMethod = Scanner(File(outputStoryFilename)).useDelimiter("\\Z").next()
    println("content: " + content)
//    println("contentNewMethod: " + contentNewMethod)
    val outStoryLines = Files.readAllLines(Paths.get(outputStoryFilename), charset)
println(outStoryLines)



    // replace vocab words w/ the same words PLUS sub/superscript info
    vocabComponentArray.forEachIndexed { index, vocabComponentArrayElement ->
        println("attempting to add indexes for vocab: " + vocabComponentArrayElement[0] )
        content = content.replace(vocabComponentArrayElement[0].toRegex(), vocabComponentArrayElement[0] + "\\\\textsubscript{" + (index+1) + "}")
    }
    Files.write(path, content.toByteArray(charset))
}


fun writeTexVocab(outputStoryWriter: PrintWriter, inputVocabFilename: String, vocabComponentArray: ArrayList<ArrayList<String>>){
    // add page title, remove indenting
    outputStoryWriter.println("\\clearpage")
    outputStoryWriter.println("\\setlength{\\parindent}{0ex}")
    outputStoryWriter.println("\\centerline{Vocabulary}")

    // print all vocab entries to page
    vocabComponentArray.forEachIndexed { index, currentSubStringWordType ->
        outputStoryWriter.println("" + (index+1) + ". " + vocabComponentArray[index][0] + " " + "\\pinyin{" + vocabComponentArray[index][1]+ "}: " + vocabComponentArray[index][2] + "\\\\")
    }
}


