package models

import collection.mutable

object MessageBoardInMemoryModel {
    private val users = mutable.Map[String, String]("Jimmy" -> "pass", "web" -> "apps", "mlewis" -> "prof")
    private val messages = List[(String, String)](("mlewis", "Hello."))
    var publicMessages = List[(String, String)](("mlewis", "I am the Scala god, all bow before me!"))
    private val messagesMap = mutable.Map[String, List[(String, String)]]("web" -> messages)
    
    //private val privateMessages = mutable.Map[String, List[MessageBoard.Message]]("Jimmy" -> List(MessageBoard.Message("Jimmy", "Mark", "Hello")))

    def validateUser(username: String, password: String): Boolean = {
        users.get(username).map(_ == password).getOrElse(false)
    }

def createUser(username: String, password: String): Boolean = {
    if(users.contains(username)) false
    else {
        users(username) = password
        true
    }
}

    def getMessages(username: String): Seq[(String, String)] = {
        messagesMap.get(username).getOrElse(Nil).toSeq

    }

    def addPrivateMessage(sender: String, receiver: String, message: String): Unit = {
        messagesMap(receiver) = (sender, message)::messagesMap.get(receiver).getOrElse(Nil)
    }

    def addPublicMessage(sender: String, message: String): Unit = {
           publicMessages = (sender, message)::publicMessages
    }
}