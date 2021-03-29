import org.scalatestplus.play._
import models._

class MessageBoardInMemoryModelSpec extends PlaySpec {
    "MessageBoardInMemoryModel" must {
        "do valid login for default user" in {
            MessageBoardInMemoryModel.validateUser("web", "apps") mustBe (true)
        }
    }
}