package logic;

import java.util.ArrayList;

import org.omg.CORBA.OMGVMCID;

public class Algoritmo {
	private static final int CANTIDAD_POBLACION=600;
	private static final double PROBABILIDAD_MUTACION=0.9;
	private static final int CANTIDAD_PADRES=150;
	private ArrayList<Individuo>poblacion;
	private int numero[];
	public Algoritmo() {
		numero=new int[2];
		setPoblacion(new ArrayList<Individuo>());
		crearPoblacion();		
		for (int i = 0; i < CANTIDAD_POBLACION; i++) {
			poblacion.get(i).calcularFitnes();
			poblacion.get(i).coincidencias();	
		}		

		bucle:
			for (int k = 0; k < 1000; k++) {	
				ArrayList<Individuo>padre=seleccionarIndividuosFitnes();
				poblacion.clear();
				for (int i = 0; i <CANTIDAD_PADRES; i++) 
					poblacion.add(padre.get(i));	
				for (int i = 0; i < CANTIDAD_POBLACION-CANTIDAD_PADRES; i++) {
					Individuo individuo=cruce(padre);		
					poblacion.add(individuo);

					if(individuo.getFitnes()==243)
						break bucle;			
				}
			}	
	}

	private void crearPoblacion(){
		int cromosoma[][]={
				{0,0,0,0,0,0,0,1,0,0,0,0},{4,0,0,0,0,0,0,0,0,0,0,0},{0,2,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,5,0,4,0,7,0,0,0},{0,0,8,0,0,0,3,0,0,0,0,0},{0,0,1,0,9,0,0,0,0,0,0,0},
				{3,0,0,4,0,0,2,0,0,0,0,0},{0,5,0,1,0,0,0,0,0,0,0,0},{0,0,0,8,0,6,0,0,0,0,0,0}
		};
		boolean cromosomaActivo[][]={
				{false,false,false,false,false,false,false,true,false,false,false,false},{true,false,false,false,false,false,false,false,false},{false,true,false,false,false,false,false,false,false,false,false,false},
				{false,false,false,false,true,false,true,false,true,false,false,false},{false,false,true,false,false,false,true,false,false,false,false,false},{false,false,true,false,true,false,false,false,false,false,false,false},
				{true,false,false,true,false,false,true,false,false,false,false,false},{false,true,false,true,false,false,false,false,false,false,false,false},{false,false,false,true,false,true,false,false,false,false,false,false}
		};
		for (int i = 0; i < CANTIDAD_POBLACION; i++) {
			poblacion.add(new Individuo());
		}
		for (int k = 0; k < poblacion.size(); k++) {		
			Individuo individuo2=poblacion.get(k);	
			for (int i = 0; i < 9; i++) {	
				for (int j = 0; j < 9; j++) {

					if(cromosoma[i][j]!=0){
						individuo2.add(i, j,cromosoma[i][j]);
						individuo2.addActivo(i,j,true);
						individuo2.addActivoEstatico(i, j,true);
					}else	{	
						individuo2.add(i,j,cromosoma[i][j]);
						individuo2.addActivo(i,j,false);
						individuo2.addActivoEstatico(i, j,false);
					}
				}

			}
		}

		for (int k = 0; k < poblacion.size(); k++) {
			Individuo individuo=poblacion.get(k);
			for (int i = 0; i < Individuo.ROW; i++) {
				for (int j = 0; j < Individuo.COL-3; j++) {
					if(!individuo.getCromosomaActivo()[i][j])
						individuo.getCromosoma()[i][j]=aleatorio(9,1);
				}		
			}
		}
	}
	private void ordenarIndividuos(){
		int i, j;
		Individuo aux=null;
		for(i=0;i<poblacion.size()-1;i++)
			for(j=0;j<poblacion.size()-i-1;j++)
				if(poblacion.get(j+1).getFitnes()<poblacion.get(j).getFitnes()){
					aux=poblacion.get(j+1);
					poblacion.set(j+1,poblacion.get(j));
					poblacion.set(j,aux);
				}
	}
	private ArrayList<Individuo> seleccionarIndividuosFitnes(){
		ordenarIndividuos();
		ArrayList<Individuo>padres=new ArrayList<>();
		for (int i = poblacion.size()-CANTIDAD_PADRES; i < poblacion.size(); i++) {
			padres.add(poblacion.get(i));
		}		
		return padres;
	}
	/**
	 * a)una parte de la madre o tra del padre corta el cromosoma en dos al alzar
	 * b)no tiene en cuenta los genotipos usa genes del padre y de la madre al azar y en orden
	 * c)corta tieniedo en cuenta los genotipos toma genotipos de la madre o delpadre al azar y en orden
	 * d)corta tieniedo en cuenta los genotipos toma genotipos de la madre o del padre de forma alternada
	 * e)para porducir mas variedad se opera con los genes dumandolos restandolos o multiplicandolos
	 * @param padre
	 * @return
	 */
	private Individuo cruce(ArrayList<Individuo>padre){

		Individuo individuo=new Individuo();
		int opcion=0,opcionNueva=0,pos1=0,pos2=0,datoAux=0,datoAuxNuevo=0;
		pos1=aleatorio(CANTIDAD_PADRES, 0);				
		pos2=aleatorio(CANTIDAD_PADRES, 0);
		pos1=numeroAleatorioRepetido(pos1,pos2,CANTIDAD_PADRES, 0);
		for (int i = 0; i < Individuo.ROW; i++) {
			for (int j = 0; j < Individuo.COL-3; j++) {
				if(padre.get(pos1).getCromosomaActivo()[i][j]==true && padre.get(pos2).getCromosomaActivo()[i][j]==true){

					individuo.getCromosomaActivo()[i][j]=true;
					individuo.getCromosoma()[i][j]=padre.get(pos1).getCromosoma()[i][j];					
				}
				if((padre.get(pos1).getCromosomaActivo()[i][j]==true && padre.get(pos2).getCromosomaActivo()[i][j]==false)
						||(padre.get(pos1).getCromosomaActivo()[i][j]==false && padre.get(pos2).getCromosomaActivo()[i][j]==true)){

					if(padre.get(pos1).getCromosomaActivo()[i][j]==true){
						individuo.getCromosomaActivo()[i][j]=true;
						individuo.getCromosoma()[i][j]=padre.get(pos1).getCromosoma()[i][j];		
					}else{
						individuo.getCromosomaActivo()[i][j]=true;
						individuo.getCromosoma()[i][j]=padre.get(pos2).getCromosoma()[i][j];		
					}						
				}
				if(padre.get(0).getCromosomaActivoEstaticas()[i][j])
					individuo.getCromosomaActivoEstaticas()[i][j]=padre.get(0).getCromosomaActivoEstaticas()[i][j];
				if(padre.get(pos1).getCromosomaActivo()[i][j]==false && padre.get(pos2).getCromosomaActivo()[i][j]==false)
				{	
					int dato=0,datoNuevo=0;
					opcionNueva=opcion;
					opcion=aleatorio(3, 1);
					opcion=numeroAleatorioRepetido(opcion,opcionNueva,3,1);
					switch (opcion) {
					case 1:			
						individuo.getCromosomaActivo()[i][j]=false;
						individuo.getCromosoma()[i][j]=aleatorio(9, 1);
						break;
					case 2:
						individuo.getCromosomaActivo()[i][j]=false;
						individuo.getCromosoma()[i][j]=padre.get(pos2).getCromosoma()[i][j];
						break;
					case 3:
						individuo.getCromosomaActivo()[i][j]=false;
						individuo.getCromosoma()[i][j]=padre.get(pos1).getCromosoma()[i][j];
						break;		
					}

				}
			}
		}
//		individuo=mutacion(individuo);		
		individuo.calcularFitnes();		
		individuo.coincidencias();
		System.out.println("INDIVIDUO--------------------");
		individuo.mostrarCromosoma();
		System.out.println("-----------------------");
		individuo.mostrarCromosomaActivo();
		return individuo;
	}
	private Individuo mutacion(Individuo individuo){
		bucle:
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if(individuo.getCromosomaActivo()[i][j]==false){
						individuo.getCromosoma()[i][j]=aleatorio(9,1);
						break bucle;
					}
				}
			}
	return individuo;
	}
	private void mostraPoblacion(){
		for (int i = 0; i < poblacion.size(); i++) {
			System.out.println("-----------------------------");
			poblacion.get(i).mostrarCromosoma();
		}

	}
	private void mostrarActivos(){
		for (int i = 0; i < poblacion.size(); i++) {
			poblacion.get(i).mostrarCromosomaActivo();
			System.out.println();
		}
	}
	private void mostrarActivosEstaticos(){
		for (int i = 0; i < poblacion.size(); i++) {
			poblacion.get(i).mostrarCromosomaActivoEstatico();
			System.out.println();
		}
	}
	public int numeroAleatorioRepetido(int dato,int datoNuevo,int maximoAleatoiro,int minimo){

		if(dato==datoNuevo){
			while(dato==datoNuevo)
				dato=aleatorio(maximoAleatoiro, minimo);
			return dato;
		}else
			return dato;

	}
	private int  aleatorio(int maximo,int minimo){
		return (int) (Math.random()*maximo+minimo);
	}
	private boolean numeroParImpar(int numero){

		if (numero%2==0)
			return true;
		else
			return false;
	}
	public ArrayList<Individuo> getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(ArrayList<Individuo> poblacion) {
		this.poblacion = poblacion;
	} 


}
