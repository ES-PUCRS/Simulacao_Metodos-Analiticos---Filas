package simulacao.metodos.analiticos.filas.queues

import groovy.transform.ThreadInterrupt
import groovy.lang.Lazy

import simulacao.metodos.analiticos.filas.server.WebServer

import simulacao.metodos.analiticos.filas.App

@ThreadInterrupt
class Simulation
	extends Thread {


		public static final boolean debug = false
		public static boolean web
	// Instance variables -------------------------------------------------------------
		private static Simulation 	instance 		// Singleton instance

		private Thread 				process 		// Simulation parallel execution
	//----------------------------------------------------------------------------------

	//-Singleton Class Configuration------------

	// Start queue simulation
	def static run(String[] args) {
		if(args.size() > 0) {
			def _instance = getInstance()
			args.each{ arg -> 
				// _instance.loadProgramToMemory(arg as String)
				// _instance.loadProgram(arg as String)
				// _instance.execute(arg as String)
			}
		} else {
			// Start ui web server
			Simulation.web = false
			new WebServer().start()
		}
	}

	// Singleton access
	def static getInstance() {
		if(!instance)
			instance = new Simulation()
		return instance
	}

	// Singleton constructor

	private Simulation(){
	    this
		    .getClass()
	    	.getResource( App.propertiesPath )
	    	.withInputStream {
	        	properties.load(it)
    		}
	}

}