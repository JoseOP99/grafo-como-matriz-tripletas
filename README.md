## Descripción de Grafo como Tripletas

En este proyecto se utiliza una representación de tripletas para almacenar un grafo. Las tripletas son una forma compacta y eficiente de representar grafos dispersos, donde no todas las conexiones entre los vértices están presentes.

Cada tripleta consiste en tres partes:
- **Vértice origen:** El vértice desde donde parte la arista.
- **Vértice destino:** El vértice hacia donde se dirige la arista.
- **Peso (opcional):** Un valor que representa el peso o la capacidad de la arista, si el grafo es ponderado.

Esta representación es útil cuando el grafo es disperso y la mayoría de los vértices no están directamente conectados entre sí. Reduce el espacio de almacenamiento en comparación con una matriz de adyacencia completa y facilita operaciones como la búsqueda de aristas incidentes en un vértice específico.

