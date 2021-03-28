package controllers

import javax.inject._

import play.api.mvc._
import play.api.i18n._
import models.MessageBoardInMemoryModel

//case class Message =(to: String, from: String, messahe: String)

@Singleton
class MessageBoard @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
    
    def login = Action {implicit request =>
        Ok(views.html.login1())
    }

    def validateLoginPost = Action {implicit request =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args =>
            val username = args("username").head
            val password = args("password").head
            if (MessageBoardInMemoryModel.validateUser(username, password)) {
                Redirect(routes.MessageBoard.messages()).withSession("username" -> username)
            }
            else {
                Redirect(routes.MessageBoard.login()).flashing("error" -> "Invalid username and password combination.")
            }
        }.getOrElse(Redirect(routes.MessageBoard.login()))
    }

    def createUser = Action {implicit request =>
        val postVals = request.body.asFormUrlEncoded
        postVals.map { args =>
            val username = args("username").head
            val password = args("password").head
            if (MessageBoardInMemoryModel.createUser(username, password)) {
                Redirect(routes.MessageBoard.login())
            }
            else {
                Redirect(routes.MessageBoard.login()).flashing("error" -> "User creation failed.")
            }
        }.getOrElse(Redirect(routes.MessageBoard.login()))
    }

    def logout = Action {
        Redirect(routes.MessageBoard.login()).withNewSession
    }

    def messages = Action {implicit request =>
        val usernameOption = request.session.get("username")
        usernameOption.map {username =>
            val messages = MessageBoardInMemoryModel.getMessages(username)
            Ok(views.html.messageBoard(messages, MessageBoardInMemoryModel.publicMessages))
        }.getOrElse(Redirect(routes.MessageBoard.login()))
    }

    def sendPrivateMessage = Action { implicit request =>
        val senderOption = request.session.get("username")
        senderOption.map { sender =>
            val postVals = request.body.asFormUrlEncoded
            postVals.map {args =>
                val receiver = args("messageReceiver").head
                val message = args("privateMessage").head
                MessageBoardInMemoryModel.addPrivateMessage(sender, receiver, message)
                Redirect(routes.MessageBoard.messages())
            }.getOrElse(Redirect(routes.MessageBoard.messages()))
        }.getOrElse(Redirect(routes.MessageBoard.login()))
    }

    def sendPublicMessage = Action {implicit request =>
        val senderOption = request.session.get("username")
        senderOption.map { sender =>
            val postVals = request.body.asFormUrlEncoded
            postVals.map {args =>
                val message = args("publicMessage").head
                MessageBoardInMemoryModel.addPublicMessage(sender, message)
                Redirect(routes.MessageBoard.messages())
            }.getOrElse(Redirect(routes.MessageBoard.messages()))
        }.getOrElse(Redirect(routes.MessageBoard.login()))
    }
}