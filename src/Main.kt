import java.io.File
import java.util.*
import java.util.ArrayList





/**
 * Created by james-clark-5 (Idios) on 12/06/17.
 *
 * Description:
 */



// TODO  - use hashing to speed up searches

// TODO - improve wordTypeSolver - here are some ideas:
// TODO  - try using markup rules e.g. (v/n + n -> v + n), see how accuracy is
// should extract adjective information (incase someone writes e.g. "buy a fast cart")
// "the", "a", "some" will be followed by a noun
// if first character of substring is a captial, assume the word is proper noun (name!) @markup@
// TODO - markup time (weekdays, am,pm,....)


fun main(args: Array<String>) {
    val dictionaryFile: String = "res/gcide-entries.xml" // http://rali.iro.umontreal.ca/GCIDE/
    var unsplitString: String = "The mouse ran up the wall for some wine"
    var splitStrings: List<String> = unsplitString.split(" ") // split string up
    var dictMatch: ArrayList<String> = ArrayList<String>() // initialise for storage
    var ignorableStrings: List<String> = Arrays.asList("The", "A", "And", "Some", "the", "a", "some", "and")
    var ignoredWordArrayList: ArrayList<String> = ArrayList<String>(Collections.singletonList("ignore")) // for "the", "a", etc.
    var emptyWordArrayList: ArrayList<String> = ArrayList<String>(Collections.singletonList("")) // for initialisation

  // get wordType for each substring

    // can ignore some words (the, a, some,...)

    // splitStrings.forEachIndexed { index, currentSubString ->
    // dont add entries, either create them at index or dont
    for(currentSubString in splitStrings) {
        if (ignorableStrings.contains(currentSubString)) {
            splitStringsWordTypes.add(ignoredWordArrayList)
        }
        else{
            splitStringsWordTypes.add(emptyWordArrayList)
        }
    }




    // get wordType WITHOUT dictionary (time)
    // get wordType WITHOUT dictionary (name)

    // get words ready for dictionary checking, then check
    var substringWordTypesIndex: Int = 0 // keep track of which word has been examined
        splitStrings.forEachIndexed { index, currentSubString ->
            println(index)
            println(splitStringsWordTypes[index])
            println(ignoredWordArrayList)
println("which word is being worked on: substringWordTypesIndex" + substringWordTypesIndex)
            if (splitStringsWordTypes[index] == ignoredWordArrayList){
                substringWordTypesIndex+=1 // keep track of which words have been worked on
                // do nothing
            }
            if (!(splitStringsWordTypes[index] == ignoredWordArrayList)) { // TODO OR [time] OR [name]
println("splitStringWordType isn't ignorable/time/person")
                // capitalise first letter
                // var currentSubStringCapitalised: String // initalise
                // prepareSubstring(currentSubString, currentSubStringCapitalised)
                var currentSubStringCapitalised: String = currentSubString.substring(0, 1).toUpperCase() + currentSubString.substring(1)
                // depluralise:
                if (currentSubStringCapitalised[currentSubStringCapitalised.length - 1] == 's') {
                    currentSubStringCapitalised = currentSubStringCapitalised.substring(0, currentSubStringCapitalised.length - 1)
                }

                // get wordType from dictionary
                findWordInDictionary(dictionaryFile, currentSubStringCapitalised, dictMatch)
                getWordType(dictMatch, currentSubString, ignoredWordArrayList,substringWordTypesIndex) // don't pass in capitalised substring
                substringWordTypesIndex +=1 // keep track of which words have been worked on
            }
        }



    println(unsplitString)
    wordTypeSolver(splitStringsWordTypes)


    //markupWord(splitStringsWordTypes,splitStrings)
}

object splitStringsWordTypes : ArrayList<ArrayList<String>>() {
}

object tempSplitStringsWordTypes : ArrayList<ArrayList<String>>() {
}

fun wordTypeSolver(splitStringsWordTypes : ArrayList<ArrayList<String>>){
    var prepArray: ArrayList<String> = ArrayList<String>(); prepArray.add("prep.")
    var nounArray: ArrayList<String> = ArrayList<String>(); nounArray.add("n.")
    var verbArray: ArrayList<String> = ArrayList<String>(); verbArray.add("v.")
    var alreadyVerb: Boolean = false

    splitStringsWordTypes.forEachIndexed { index, currentSubStringType ->

        // use prep. information first
        // assume possible prep. is definitely prep.
        // assume prep. is followed by noun
        // note: sentence could be "need to purchase food"; this algorithm would give [v.],[prep.],[n.],[n.]
        // however, the user should use be concise and say "purchase food"
        // this algorithm should be safe in that case, and applies well to e.g. "take cake to party"
        // TODO - should run this prep. part over all words, and THEN handle the less definite stuff
        if (splitStringsWordTypes[index].contains("prep.")){
            // only keep the prep. part
            splitStringsWordTypes[index] = prepArray
            // next n. is definitely n.
            if (splitStringsWordTypes[index+1].contains("n.")){ // TODO convert this to a loop, just in case!
                splitStringsWordTypes[index+1] = nounArray
            }
            else if (splitStringsWordTypes[index+2].contains("n.")){
                splitStringsWordTypes[index+2] = nounArray
            }
        }
        // assume that v. comes first
        // TODO make some less janky code/logic
        if (splitStringsWordTypes[index].contains("v.") && alreadyVerb == false){ //strange logic, needs ironing out
            splitStringsWordTypes[index] = verbArray
            alreadyVerb = true
        }
        // assume that any leftovers are nouns
        if (splitStringsWordTypes[index].contains("n.")){
            splitStringsWordTypes[index] = nounArray
            alreadyVerb = true
        }
        // n.b. some of this algorithm is redundant under the assumption that verb comes first
    }
    println(splitStringsWordTypes)
}

fun findWordInDictionary(fileName: String, inputWord: String, dictMatch: ArrayList<String>){
    val file: File = File(fileName) // get file ready
    val scan: Scanner = Scanner(file)
    val stringToFind: String = "<entry key=\"" + inputWord + "\">"
    val terminationString: String = "</entry>"
    var saveLinesBoolean: Boolean = false
    var vmorphBoolean: Boolean = false
    dictMatch.clear() // TODO tidy this up; can probably use String instead of ArrayList<String> (having the lines in in an array isn't important)

//    println("create ArrayList starting with string: " + stringToFind)

// find match in dictionary
    while(scan.hasNextLine()) {
        val line: String = scan.nextLine() // read all lines
        if (line.contains(stringToFind)) {              // found a matching dictionary entry for String
            saveLinesBoolean = true
        }
        if (line.contains(terminationString) && saveLinesBoolean == true) { // found ending string
            saveLinesBoolean = false
        }

        // don't use vmorph information (this is for modified versions of the base word, which we don't need)
        if (line.contains("<vmorph>") && saveLinesBoolean == true) { //
            vmorphBoolean = false
        }
        if (line.contains("</vmorph>")) {              // found a matching dictionary entry for String
            vmorphBoolean = true
        }

        // read lines and save to variable, until </entry> is met
        if (saveLinesBoolean==true && vmorphBoolean==true)
            dictMatch.add(line)
        }
    scan.close()
println("dictMatch is:" + dictMatch)
}

fun getWordType(dictMatch:ArrayList<String>, inputWord: String, ignoredWordArrayList: ArrayList<String>, substringWordTypesIndex: Int) {
    // TODO keep track of which substring is being worked on :
    // if splitStringsWordTypes[index] == "



    var dictMatchString: String = dictMatch.toString()
    var allCurrentWordTypes: ArrayList<String> = ArrayList<String>()

    while (dictMatchString.contains("<pos>")) {
        // look to see if there is (still) a wordType
        val typeStartPos: Int = dictMatchString.indexOf("<pos>")
        val typeEndPos: Int = dictMatchString.indexOf("</pos")
        val wordType: String = dictMatchString.substring(typeStartPos + 5, typeEndPos) // get word type

        var dictMatchStringSize: Int = dictMatchString.length // cant get this below, due to shortening of string
        dictMatchString = dictMatchString.substring(0, typeStartPos) + dictMatchString.substring(typeEndPos + 6, dictMatchStringSize);

        if (wordType.contains("n.") && !allCurrentWordTypes.contains("n.")) {
            allCurrentWordTypes.add("n.")
        }

        if (wordType.contains("v.") && !allCurrentWordTypes.contains("v.")) { // doesnt seem to be working
            allCurrentWordTypes.add("v.")
        }

        if (wordType.contains("prep.") && !allCurrentWordTypes.contains("prep.")) {
            allCurrentWordTypes.add("prep.")
        }
    }
    tempSplitStringsWordTypes.add(allCurrentWordTypes) // add these to the global arrray of word types
    //dictMatchString no longer contains <pos>
    println("allCurrentWordTypes" + allCurrentWordTypes)
    println("tempSplitStringsWordTypes" + tempSplitStringsWordTypes)
    println("adding entry to splitStringsWordTypes!")

                // @PESUDOCODE@ ADD tempSplitStringsWordTypes TO splitStringsWordTypes, IF splitStringsWordTypes[INDEX]!=ignore
    if(splitStringsWordTypes[substringWordTypesIndex]==ignoredWordArrayList) {
    //do nothing...
    }
    else {
        splitStringsWordTypes[substringWordTypesIndex] = allCurrentWordTypes
        println("splitStringsWordTypes" + splitStringsWordTypes)
    }
}


fun markupWord(splitStringsWordTypes: ArrayList<ArrayList<String>>, splitStrings: List<String>) {
    var preVerb: String = "[" ; var postVerb: String = "]"
    var preNoun: String = "#" ; var postNoun: String = "#"
    var markedSplitString: String
    var markedString: String = ""
    splitStringsWordTypes.forEachIndexed { index, currentSubStringWordType ->
        //println("$currentSubStringType at $index")
        if (splitStringsWordTypes[index].contains("v.")) {
            markedSplitString = splitStrings.get(index)//
            markedSplitString = preVerb + markedSplitString + postVerb
        }
        else if (splitStringsWordTypes[index].contains("n.")) {
            markedSplitString = splitStrings.get(index)//
            markedSplitString = preNoun + markedSplitString + postNoun
        } else { // don't markup
            markedSplitString = splitStrings.get(index)
        }
        markedString = markedString + " " + markedSplitString
        //outputWord(markedString)
    }
    println(markedString)
}

fun outputWord(markedString: String) {
//try {
//    val outputFileName = "output/" + "uniquePinyinIn_" + fileName
//    val writer = PrintWriter(outputFileName, "UTF-8")
//    writer.println("Number of unique pinyin:"+uniquePinyinArrayList.size +", Total number of pinyin:" + pinyinArrayList.size)
//    writer.close()
    println(markedString)
//} catch (e: IOException) {
// do something
}

//}

