package simulacao.metodos.analiticos.filas.server

import com.sun.net.httpserver.*
import groovy.lang.Lazy

import simulacao.metodos.analiticos.filas.ui.Render
import simulacao.metodos.analiticos.filas.ui.ANSI
import simulacao.metodos.analiticos.filas.App

class WebServer
	extends Thread {

	@Lazy
	private static Properties properties
	private static WebServer  instance	// Singleton instance
	
	def static importProperties(){
		new Object() {}
	    	.getClass()
	    	.getResource( App.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
	    	}
	}

	private WebServer(){ importProperties() }

	// Singleton access
	def static getInstance() {
		if(!instance)
			instance = new WebServer()
		return instance
	}

	public void run() {
		getInstance()

		final TYPES = [
			"css"	: "text/css",
			"html"	: "text/html",
			"jpg"	: "image/jpeg",
			"js"	: "application/javascript",
			"png"	: "image/png",
			"ico"	: "image/x-icon"
		]

		def port = (properties?."server.port" as Integer) ?: 2345
		def views = new File(properties."ui.views.path")
		def server = HttpServer.create(new InetSocketAddress(port), 0)

		server.createContext("/", { HttpExchange exchange ->
			try {
				
				if (!"GET".equalsIgnoreCase(exchange.requestMethod)) {			
			        exchange.sendResponseHeaders(405, 0)
					exchange.responseBody.close()
					return
				}
				
				def params = [:]
				def path = exchange.requestURI.path
				def exchangeRequestURI = (exchange.requestURI as String)

				/*
				 * -Exchange parser parameters to params<map>	
				 * 	Example: request /shell.html?foo=bar&fus=rodah
				 *		params[foo] == bar
				 *		params[fus] == rodah
				 *		...
				 */
				if ((exchangeRequestURI.charAt(exchangeRequestURI.length()-1)) != "?")
					if (exchangeRequestURI.contains("?")) {
						params = exchangeRequestURI
							.replaceAll(".*\\?","")
							.split('&')
							.inject([:]) { map, token -> 
	    	    				token.split('=').with {
	        						map[it[0]?.trim()] = (it[1]?.trim()?.replace("%20","")?.split(','))
	    						}
	    						map
							}
					}

				/*
				 *	If request for console or favicon, do not print on console log
				 *	Otherwise, track logs as 'GET / -> Params: [:]'
				 *	REST Method and URI Path will be shown green
				 */
				String route = path.substring(1)
				String view  = path.replaceAll(".*/","")
				if(!path.contains(".css") && !path.contains("favicon") && !route.isEmpty())
					println "${ANSI.GREEN}$exchange.requestMethod $path${ANSI.RESET} -> Params: $params"

				/*
				 *	Validate if it is just a .ico or .css request
				 *	And create the correct render accordingly with the path 
				 */
				def file
				def render
				if (path.contains(".ico")) {
					file = new File(properties."ui.assets.path", route)
				} else if (path.contains(".css")) {
					file = new File(views, "${route}/styles.css")
					if (!file.exists()) file = new File(views, "http/404/styles.css")
				} else {
					try {
						if(route.isEmpty()) file = new File(views, "shell/shell.html")
						else file = new File(views, "${route}/${view}.html")
					} catch (e) { e.printStackTrace() }
					render = Render.handler(view, params)
				}

				
				/*
				 *	Return to caller the page or handler found.
				 *	If non was found or contains on the project,
				 *	404 HTTP/1.1 Client error will be thrown.
				 */
				if (file.exists() || render) {
					exchange.responseHeaders.set(
						"Content-Type",
						TYPES[file.name.split(/\./)[-1]] ?: "text/plain"
					)
			        exchange.sendResponseHeaders(200, 0)

			        if(render){
						exchange.responseBody << render
			        } else {
				        file.withInputStream {
							exchange.responseBody << it
				        }
				    }

					exchange.responseBody.close()
				} else {
			        exchange.sendResponseHeaders(404, 0)
					new File(views, "http/404/page.html")
						.withInputStream {
							exchange.responseBody << it
			        	}
					exchange.responseBody.close()
				}

			} catch(e) {
				println e.getLineNumber()
				println e.getMessage()
			}

		} as HttpHandler)

		server.start()
		println "Web Server started on port ${port}"
	}
}