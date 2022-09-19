package simulacao.metodos.analiticos.filas.ui

import groovy.text.SimpleTemplateEngine
import groovy.lang.Lazy

import simulacao.metodos.analiticos.filas.ui.views.queues.*
import simulacao.metodos.analiticos.filas.ui.views.*
import simulacao.metodos.analiticos.filas.App

class Render{

	@Lazy
	private static Properties properties
	private static final String views = importProperties()

	def static handler(def view, def map) {
		def result
		try {
			if(!view.isEmpty())
				result = "${view}"(map)
		} catch (ignore) {
			if(properties."app.debug" as boolean)
				ignore.printStackTrace()
		}
		result
	}

	def static queues (def map) {
		def file = new File(views, "queues/page.html")
		def writeList 		= ""
		def readList 		= ""
		def response		= ""
		
		if(!map.isEmpty())
			map.each{ key, value -> 
				response += key
			}

		
		def binding = [
			'writeList' : writeList,
			'readList': readList
		]
		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
	}

	def static shell(def map) { null }
	
	// DEVTOOLS
	def static restart(def map) {
		Runtime.
			getRuntime().
		   		exec("cmd /c start \"\" DevTools.bat 0");
	}

	def static commit(def map) {
		Runtime.
			getRuntime().
		   		exec("cmd /c start \"\" DevTools.bat 1 \"${map["comment"]}\"");
	}
	// ----


	def static favicon(def map) {
		def file = new File(("${views}/assets"), "template.html")
	}

	def static importProperties() {
		new Object() {}
	    	.getClass()
	    	.getResource( App.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
	    	}
	    properties."ui.views.path"
	}
}
