class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/registration"(controller:"registration", action: "index")
        "/registration/activate/$activationKey?"(controller: "registration", action: "activate")

        "/games"(controller:"boardGame", action: "index")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
