class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        // Clean URLs are Important
        "/about"(controller:"about", action: "index")
        "/registration"(controller:"registration", action: "index")
        "/registration/activate/$activationKey?"(controller: "registration", action: "activate")

        "/games"(controller:"boardGame", action: "index")
        "/collection"(controller:"collection", action: "index")

        // REST API
        "/rest/1.0/boardgame/user/$username?/$objectId?" {
            controller = "boardGame"
            action = [POST: "assignBoardGameToUser", DELETE: "removeBoardGameFromUser"]
        }
        "/rest/1.0/boardgame/user/$username?" {
            controller = "boardGame"
            action = [GET: "getAllBoardGamesForUser"]
        }
        "/rest/1.0/boardgame/search/$searchKeyword?" {
            controller = "boardGame"
            action = [GET: "searchBoardGames"]
        }
        "/rest/1.0/boardgame/$objectId?" {
            controller = "boardGame"
            action = [GET: "getBoardGameDetails"]
        }
        "/rest/1.0/boardgame" {
            controller = "boardGame"
            action = [GET: "getAllBoardGames"]
        }

        "/"(view:"/index")
        "500"(view:'/error')
    }
}
