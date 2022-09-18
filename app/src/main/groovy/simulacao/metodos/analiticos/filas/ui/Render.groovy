package simulacao.metodos.analiticos.filas.ui

import groovy.lang.Lazy

import simulacao.metodos.analiticos.filas.ui.views.*
import simulacao.metodos.analiticos.filas.App

class Render{

	@Lazy
	private static Properties properties
	private static final String views = importProperties()

	def static handler(String view, def map) {
		def result
		try {
			result = "view".render(map);
		} catch (e) {}
		println result
		result
	}

	class shell {
		def static render(def map) { null }
	}
	
	// DEVTOOLS
	class restart {
		def static render(def map){
			Runtime.
				getRuntime().
			   		exec("cmd /c start \"\" DevTools.bat 0");
		}
	}

	class commit {
		def static render(def map) {
			Runtime.
				getRuntime().
			   		exec("cmd /c start \"\" DevTools.bat 1 \"${map["comment"]}\"");
		}
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
