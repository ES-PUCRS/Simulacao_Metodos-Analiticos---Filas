package simulacao.metodos.analiticos.filas.ui.views.queues

import simulacao.metodos.analiticos.filas.App

import groovy.text.SimpleTemplateEngine 
import groovy.lang.Lazy

class page {

	@Lazy
	private static Properties properties
	private static final String views = importProperties()
	private static final String pageName = "queues"

	def static render(def map) {
		def file = new File(views, "${pageName}/page.html")
		def writeList 		= ""
		def readList 		= ""
		def response		= ""
		
		if(!map.isEmpty())
			map.each{ key, value -> 
				response += key
			}

		// def disabled
		// pm.filterByIORequest(IOREQUEST.READ).each { block ->
		// 	if(block.getProcessName().equals(response))
		// 		 disabled = "disabled"
		// 	else disabled = ""
		// 	readList +=
		// 	"""
		// 		<form id="readForm" action="/unblock">
		// 			<label for="block.getProcessName()">${block.getProcessName()}</label>
		// 			<br>
		// 			<input type="number" id="${block.getProcessName()}" name="${block.getProcessName()}" ${disabled}>
		// 			<br><br>
		// 		</form>
		// 	"""
		// }
		// if(readList.equals(""))
		// 	readList = "<p>There is no blocked process waiting to be read</p>"

		// def disabledField
		// pm.filterByIORequest(IOREQUEST.WRITE).each { block ->
		// 	if(block.getProcessName().equals(response)){
		// 		disabledField = "disabled"
		// 		disabled = "disabled"
		// 	} else {
		// 		disabled = "readonly"
		// 		disabledField = ""
		// 	}
		// 	writeList +=
		// 	"""
		// 		<form id="writeForm" action="/unblock">
		// 			<label for="block.getProcessName()">${block.getProcessName()}</label>
		// 			<br>
		// 			<input class="textField" type="text" id="${block.getProcessName()}" name="${block.getProcessName()}" value="${block.getIoRegisters()[1]}" ${disabled}>
		// 			<input class="textFieldButton" type="submit" value="Free" ${disabledField}>
		// 			<br><br>
		// 		</form>
		// 	"""
		// }
		// if(writeList.equals(""))
		// 	writeList = "<p>There is no blocked process waiting to be written</p>"

		
		def binding = [
			'writeList' : writeList,
			'readList': readList
		]
		new SimpleTemplateEngine()
			.createTemplate(file)
			.make(binding)
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