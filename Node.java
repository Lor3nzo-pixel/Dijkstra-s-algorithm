import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Node implements Comparable<Node>{
    private String nome = "";
    private Integer distanza = Integer.MAX_VALUE;
    private List<Node> percorsoMinore;
    private Map<Node, Integer> nodiAdiacenti;

    public Node(String nome){
        this.nome = nome;
        distanza = Integer.MAX_VALUE;
        percorsoMinore = new LinkedList<>();
        nodiAdiacenti = new HashMap<>();
    }

    public void addNodoAdiacente(Node nodo, int peso){
        nodiAdiacenti.put(nodo, peso);
    }


    @Override
    public int compareTo(Node nodo) {
        return Integer.compare(this.distanza, nodo.getDistanza());
    }

    public static void ShortestPathCalculation(Node sorgente){
        sorgente.setDistanza(0);
        Set<Node> nodiVisitati = new HashSet<>();
        Queue<Node> nodiNonVisitati = new PriorityQueue<>(Collections.singleton(sorgente));
        while(!nodiNonVisitati.isEmpty()){
            Node nodoCorrente = nodiNonVisitati.poll();
            nodoCorrente.getNodiAdiacenti()
                    .entrySet().stream()
                    .filter(entry -> !nodiVisitati.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), nodoCorrente);
                        nodiNonVisitati.add(entry.getKey());
                    });
            nodiVisitati.add(nodoCorrente);
        }
    }

    private static void evaluateDistanceAndPath(Node nodoAdiacente, Integer peso, Node nodoSoregente) {
        Integer newDistanza = nodoSoregente.getDistanza() + peso;
        if(newDistanza < nodoAdiacente.getDistanza()){
            nodoAdiacente.setDistanza(newDistanza);
            nodoAdiacente.setPercorsoMinore(
                    Stream.concat(nodoSoregente.getPercorsoMinore().stream(), Stream.of(nodoSoregente)).toList()
            );
        }
    }

    //Get nome
    public String getNome() {
        return nome;
    }

    //Get + set distanza
    public Integer getDistanza() {
        return distanza;
    }
    public void setDistanza(Integer distanza) {
        this.distanza = distanza;
    }

    //Get+ set nodi adiacenti
    public Map<Node, Integer> getNodiAdiacenti() {
        return nodiAdiacenti;
    }
    public void setNodiAdiacenti(Map<Node, Integer> nodiAdiacenti) {
        this.nodiAdiacenti = nodiAdiacenti;
    }

    //Get + set percorso minore

    public List<Node> getPercorsoMinore() {
        return percorsoMinore;
    }
    public void setPercorsoMinore(List<Node> percorsoMinore) {
        this.percorsoMinore = percorsoMinore;
    }

    private static void printPaths(List<Node> nodi){
        nodi.forEach(nodo -> {
            String path = nodo.getPercorsoMinore().stream()
                    .map(Node::getNome)
                    .collect(Collectors.joining(" -> "));
            System.out.println((path.isBlank()
                    ? "%s : %s".formatted(path, nodo.getNome(), nodo.getDistanza())
                    : "%s -> %s : %s".formatted(path, nodo.getNome(), nodo.getDistanza()))
            );
        });
    }

    //Test con esempio trattato nel documento su classroom
    public static void main(String[] args) {
        Node nodoInizio = new Node("Casa");
        Node nodoA = new Node("A");
        Node nodoB = new Node("B");
        Node nodoC = new Node("C");
        Node nodoD = new Node("D");
        Node nodoE = new Node("E");
        Node nodoFine = new Node("Ufficio");

        nodoInizio.addNodoAdiacente(nodoA, 2);
        nodoInizio.addNodoAdiacente(nodoD, 8);

        nodoA.addNodoAdiacente(nodoB, 6);
        nodoA.addNodoAdiacente(nodoC, 2);

        nodoD.addNodoAdiacente(nodoC, 2);
        nodoD.addNodoAdiacente(nodoE, 3);

        nodoC.addNodoAdiacente(nodoD, 2);
        nodoC.addNodoAdiacente(nodoE, 9);

        nodoB.addNodoAdiacente(nodoFine, 5);
        nodoE.addNodoAdiacente(nodoFine, 1);

        Node.ShortestPathCalculation(nodoInizio);
        Node.printPaths(Arrays.asList(nodoInizio, nodoA, nodoB, nodoC, nodoD, nodoE, nodoFine));
    }
}
