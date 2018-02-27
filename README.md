# Graded-Reader-Builder
*Graded Reader Builder lets people create professional-quality graded readers from simple text input (story, vocab, characters, etc.). A wealth of high-quality books written by language learners, for language learners can be produced by this tool - and can be openly shared on an open-source graded reader database website (todo).

I built this in IntelliJ IDEA, with Kotlin. I was using *[TeXstudio](http://www.texstudio.org/)* and *[MiKTEX](https://miktex.org/download)*. 

You may need to: 
* Install packages (mostly for CJK/pinyin)
* Compile with XeLaTeX (Options -> configure TeXstudio -> build tab (on left) -> default compiler: XeLaTeX)

TODO: 
* Check which packages I've used, and give credit where it's due
* Set up a wiki-like site where user-created content (after review, to prevent copyright infringement?) can be searched e.g. by level, length, rating, etc.
* Set up a server that can take input (story, vocab, characters, etc.) and securely process it so that users can use this tool without having to download either the code or any required programs.
* Set up a licence for this - likely as free as possible, as it's probable that someone can write a better implementation anyway.
* Generalise code (if I remember correctly, I had everything named for making a Chinese graded reader for an English speaker).
* Allow input from a github repo (story, vocab, etc.); this would allow for easy correction/modification by other language learners.

**Please make requests/suggestions & report issues with the GitHub "Issue" tool, or do a Pull Request. Even if you help with something small, you can help this tool reach it's potential! Thanks!**
