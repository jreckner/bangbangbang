modules = {
    application {
        resource url:'js/application.js'
    }
    customBootstrap {
        resource url: '/less/custom-bootstrap.less', attrs:[rel: "stylesheet/less", type:'css'], disposition: 'head'
    }
}