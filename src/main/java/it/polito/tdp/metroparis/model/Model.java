package it.polito.tdp.metroparis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	private Graph<Fermata, DefaultEdge> grafo;
	
	public void creaGrafo() {
		// istanziamo la struttura dati (il grafo stesso)
		this.grafo = new SimpleDirectedGraph<Fermata, DefaultEdge>(DefaultEdge.class);
		
		MetroDAO dao = new MetroDAO();
		List<Fermata> fermate = dao.getAllFermate();
		Map<Integer, Fermata> fermateIdMap = new HashMap<Integer, Fermata>();
		
		for (Fermata f : fermate) {
			fermateIdMap.put(f.getIdFermata(), f);
		}
		
		Graphs.addAllVertices(this.grafo, fermate);
		
		// METODO 1: itero su ogni coppia di vertici (NON SEMPRE e' il piu' lento!
		//										      es. grafi piccoli)
		/*
		for (Fermata partenza : fermate) {	// this.grafo.vertexSet()
			for (Fermata arrivo : fermate) {
				if (dao.isFermateConnesse(partenza, arrivo)) {
					// esiste almeno una connessione tra partenza e arrivo
					this.grafo.addEdge(partenza, arrivo);
				}
			}
		}
		*/
		
		// METODO 2: dato un vertice, trova quelli ad esso adiacenti
		// versione 2a: il dao restituisce una lista di id numerici
		/*
		for (Fermata partenza : fermate) {
			// ho un vertice (partenza)
			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
			for (Integer id : idConnesse) {
				// ho un vertice adiacente (arrivo)
				Fermata arrivo = null;
				for (Fermata f : fermate) {
					if (f.getIdFermata() == id) {
						arrivo = f;
						break;
					}
				}
				this.grafo.addEdge(partenza, arrivo);
				
			}
		}
		*/
		// versione 2b: il dao restituisce una lista di oggetti Fermata
		/*
		for (Fermata partenza : fermate) {
			List<Fermata> arrivi = dao.getFermateConnesse(partenza);	// TODO creare nuovo metodo
																		// nel dao
			for (Fermata arrivo : arrivi) {
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/
		// versione 2c: il dao restituisce una lista di id numerici, che converto in
		// oggetti Fermata tramite una mappa (identity map)
		/*
		for (Fermata partenza : fermate) {
			List<Integer> idConnesse = dao.getIdFermateConnesse(partenza);
			for (Integer id : idConnesse) {
				Fermata arrivo = fermateIdMap.get(id);
				this.grafo.addEdge(partenza, arrivo);
			}
		}
		*/

		// METODO 3: faccio un'unica query che mi restituisca le coppie di fermate connesse
		// variante preferita: 3c (identity map)
		List<CoppiaId> fermateDaCollegare = dao.getAllFermateConnesse();
		for (CoppiaId c : fermateDaCollegare) {
			Fermata partenza = fermateIdMap.get(c.getIdPartenza());
			Fermata arrivo = fermateIdMap.get(c.getIdArrivo());
			this.grafo.addEdge(partenza, arrivo);
		}

		
		// System.out.println(this.grafo);
		System.out.println("Vertici: " + this.grafo.vertexSet().size());
		System.out.println("Archi: " + this.grafo.edgeSet().size());

		this.visitaGrafo(fermate.get(0));
	}

	public void visitaGrafo(Fermata partenza) {
		GraphIterator<Fermata, DefaultEdge> visita = 
				new DepthFirstIterator<Fermata, DefaultEdge>(this.grafo, partenza);
		while(visita.hasNext()) {
			Fermata f = visita.next();
			System.out.println(f);
		}
	}
}
