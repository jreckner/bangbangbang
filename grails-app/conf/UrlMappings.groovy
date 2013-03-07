class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/registration"(controller:"registration", action: "index")

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
