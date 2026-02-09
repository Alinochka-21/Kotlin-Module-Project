import java.util.Scanner
object GeneralLogic{
    val archiveList : MutableList<NotesArchive> = mutableListOf()
    enum class appLevel{
        MAIN,
        ARCHIVE,
        NOTE
    }
    var levelApp:appLevel = appLevel.MAIN  // "ARCHIVE" "NOTE"
    var  choiceArchive: NotesArchive? = null
    var  choiceNote: Note? = null
    var exiteApp: Boolean = false

    fun choiceCommand(){
        when (levelApp){
            appLevel.MAIN -> {
                println("Вы в главном меню")
                println("Выберите команду, надо написать число:")
                println("0 - Выход\n1 - Создать Архив\n2 - Показать список созданных Архивов")
                this.command()
            }
            appLevel.ARCHIVE -> {
                println("Вы в архиве \'${choiceArchive!!.name}\'")
                println("Выберите команду, надо написать число:")
                println("0 - Выход\n1 - Создать Заметку\n2 - Показать список созданных Заметок")
                this.command()
            }
            appLevel.NOTE -> {
                println("Вы в заметках архива \'${choiceArchive!!.name}\'")
                println("Выберите команду, надо написать число:")
                println("0 - Выход\n1 - Выбрать Заметку")
                var command = readInt(1,::choiceCommand)
                when (command){
                    0 -> exit()
                    1 -> {
                        println("------------")
                        println("Выберите Заметку из списка")
                        choiceArchive!!.noteList.forEachIndexed { index, obj ->
                            println("$index - ${obj.name}")
                        }
                        open(readInt(choiceArchive!!.noteList.size,::choiceCommand))
                    }
                }
            }
        }
    }
    fun command(){
        var command = readInt(2,::command)
        when (command){
            0 -> exit()
            1 -> createArchiveOrNote()
            2 ->  showList()
        }
    }
    fun exit(){
        when (levelApp){
            appLevel.NOTE -> {
                levelApp = appLevel.ARCHIVE
                choiceCommand()
            }
            appLevel.ARCHIVE -> {
                levelApp = appLevel.MAIN
                choiceNote = null
                choiceCommand()
            }
            appLevel.MAIN -> {
                println("Завершение программы")
                choiceArchive = null
                archiveList.clear()
                exiteApp = true
            }
        }
    }

    fun createArchiveOrNote(){
        when (levelApp) {
            appLevel.MAIN -> {
                var probableArchive = inputText()
                archiveList.add(NotesArchive(probableArchive))
                println("Архив добавлен в список ваших архивов")
                println("------------")
                levelApp =appLevel.MAIN
                choiceCommand()

            }
            appLevel.ARCHIVE -> {
                var probableNote = inputText()
                println("Напишите содержание заметки:")
                var content: String = inputText()
                choiceArchive!!.noteList.add(Note(probableNote, content))
                println("Заметка успешно сохранена")
                println("------------")
                this.choiceCommand()
            }
            appLevel.NOTE -> {}
        }
    }

    fun showList(){
        when (levelApp){
            appLevel.MAIN -> {
                println("Список созданных архивов:")
                if (archiveList.size == 0) {
                    println("Здесь пусто. Создайте архив")
                    println("------------")
                    this.choiceCommand()
                }
                archiveList.forEachIndexed { index, obj ->
                    println("$index -  ${obj.name}")
                }
                println("------------")
                println("Введите число, чтобы выбрать нужный Архив")
                this.open(readInt(archiveList.size,::showList))
            }
            appLevel.ARCHIVE -> {
                println("Список созданных заметок:")
                if (choiceArchive!!.noteList.size == 0) {
                    println("Здесь пусто. Создайте заметку")
                    println("------------")
                    this.choiceCommand()
                }
                choiceArchive!!.noteList.forEachIndexed { index, obj ->
                    println("$index - ${obj.name}")
                }
                levelApp = appLevel.NOTE
                println("------------")
                choiceCommand()
            }
            appLevel.NOTE -> {}
        }
    }

    fun open(number: Int){
        when (levelApp) {
            appLevel.MAIN  -> {
                println("------------")
                choiceArchive = archiveList[number]
                levelApp = appLevel.ARCHIVE
                this.choiceCommand()
            }
            appLevel.NOTE -> {
                println("------------")
                choiceNote = choiceArchive!!.noteList[number]
                println("Вы открыли заметку \"${choiceNote!!.name}\"! Хотите узнать ее содержимое?\n0 - ДА\n1 - НЕТ")
                var answer = readInt(1){open(number)}
                if (answer == 0) {
                    println("---СОДЕРЖАНИЕ---")
                    choiceNote!!.knowContent()
                    println("------------")
                    this.choiceCommand()
                }
                else if (answer==1){
                    levelApp = appLevel.ARCHIVE
                    this.choiceCommand()
                }
            }
            appLevel.ARCHIVE -> {}
        }
    }

    fun readInt(max: Int = Int.MAX_VALUE, onError: () -> Unit): Int {
        while (true) {
            val input = readLine()
            val number = input?.toIntOrNull()

            if (number == null) {
                println("Введите число, а не что-то другое")
                onError()
            } else if (number < 0 || number > max) {
                // Проверяем что >= 0 и <= max
                println("Нет команды под таким числом")
                onError()
            } else {
                return number
            }
        }
    }
    fun inputText(): String {
        println("Введите текст")
        var text: String = Scanner(System.`in`).nextLine()
        if (text.trim().isNotEmpty()){
            return text
        }
        else {
            println("Это поле не может быть пустым")
            return this.inputText()
        }
    }
}
