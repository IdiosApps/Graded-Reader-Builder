package VocabUtils

import java.io.File
import java.util.*

fun getVocabIndicies(vocabComponentArray: ArrayList<ArrayList<String>>){
    vocabComponentArray.forEachIndexed { index, vocabElement ->
        vocabElement.add(Integer.toString(index))
    }
}

fun vocabToArray(inputFilename: String, inputArray: ArrayList<String>, inputComponentArray: ArrayList<ArrayList<String>>){
    val inputFile: File = File(inputFilename) // get file ready
    val scan: Scanner = Scanner(inputFile)
    var lineCount: Int = 0
    var tmpComponentArrayList: ArrayList<String> = ArrayList<String>()
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
