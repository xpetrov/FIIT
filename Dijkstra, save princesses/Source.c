#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

#define UP	0
#define RIGHT	1
#define LOWER	2
#define LEFT	3

struct node {
	int i, j;
	int actual_remoteness;
	int flag;
	int is_in_heap;
	char value;
	int time;
	int number_of_neighbors;
	struct node **neighbors;
};

int possible_ways[6];
#define p1p2p3	0
#define p1p3p2	1
#define p2p1p3	2
#define p2p3p1	3
#define p3p1p2	4
#define p3p2p1	5

int dragon_i = INT_MAX, dragon_j = INT_MAX;
int p1_i = INT_MAX, p1_j = INT_MAX;
int p2_i = INT_MAX, p2_j = INT_MAX;
int p3_i = INT_MAX, p3_j = INT_MAX;

struct node *** nodes;	// list of graph nodes
struct node ** min_heap;
int number_of_elements_in_heap = 0;
int height, width;
int * coordinates = NULL;
int count_of_coordinates = 0;


void set_neighbors() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// 1
				if (i == 0 && j == 0) {
					nodes[i][j]->neighbors[RIGHT] = (nodes[i][j + 1]->value != 'N') ? nodes[i][j + 1] : NULL;
					nodes[i][j]->neighbors[LOWER] = (nodes[i + 1][j]->value != 'N') ? nodes[i + 1][j] : NULL;
				}
				// 2
				if (i == 0 && j == width - 1) {
					nodes[i][j]->neighbors[LEFT] = (nodes[i][j - 1]->value != 'N') ? nodes[i][j - 1] : NULL;
					nodes[i][j]->neighbors[LOWER] = (nodes[i + 1][j]->value != 'N') ? nodes[i + 1][j] : NULL;
				}
				// 3
				if (i == height - 1 && j == 0) {
					nodes[i][j]->neighbors[RIGHT] = (nodes[i][j + 1]->value != 'N') ? nodes[i][j + 1] : NULL;
					nodes[i][j]->neighbors[UP] = (nodes[i - 1][j]->value != 'N') ? nodes[i - 1][j] : NULL;
				}
				// 4
				if (i == height - 1 && j == width - 1) {
					nodes[i][j]->neighbors[LEFT] = (nodes[i][j - 1]->value != 'N') ? nodes[i][j - 1] : NULL;
					nodes[i][j]->neighbors[UP] = (nodes[i - 1][j]->value != 'N') ? nodes[i - 1][j] : NULL;
				}
				// 5
				if (i == 0 && j > 0 && j < width - 1) {
					nodes[i][j]->neighbors[LEFT] = (nodes[i][j - 1]->value != 'N') ? nodes[i][j - 1] : NULL;
					nodes[i][j]->neighbors[RIGHT] = (nodes[i][j + 1]->value != 'N') ? nodes[i][j + 1] : NULL;
					nodes[i][j]->neighbors[LOWER] = (nodes[i + 1][j]->value != 'N') ? nodes[i + 1][j] : NULL;
				}
				// 6
				if (i == height - 1 && j > 0 && j < width - 1) {
					nodes[i][j]->neighbors[LEFT] = (nodes[i][j - 1]->value != 'N') ? nodes[i][j - 1] : NULL;
					nodes[i][j]->neighbors[RIGHT] = (nodes[i][j + 1]->value != 'N') ? nodes[i][j + 1] : NULL;
					nodes[i][j]->neighbors[UP] = (nodes[i - 1][j]->value != 'N') ? nodes[i - 1][j] : NULL;
				}
				// 7
				if (i > 0 && i < height - 1 && j == 0) {
					nodes[i][j]->neighbors[UP] = (nodes[i - 1][j]->value != 'N') ? nodes[i - 1][j] : NULL;
					nodes[i][j]->neighbors[RIGHT] = (nodes[i][j + 1]->value != 'N') ? nodes[i][j + 1] : NULL;
					nodes[i][j]->neighbors[LOWER] = (nodes[i + 1][j]->value != 'N') ? nodes[i + 1][j] : NULL;
				}
				// 8
				if (i > 0 && i < height - 1 && j == width - 1) {
					nodes[i][j]->neighbors[LOWER] = (nodes[i + 1][j]->value != 'N') ? nodes[i + 1][j] : NULL;
					nodes[i][j]->neighbors[LEFT] = (nodes[i][j - 1]->value != 'N') ? nodes[i][j - 1] : NULL;
					nodes[i][j]->neighbors[UP] = (nodes[i - 1][j]->value != 'N') ? nodes[i - 1][j] : NULL;
				}
				// 9
				if (i > 0 && i < height - 1 && j > 0 && j < width - 1) {
					nodes[i][j]->neighbors[UP] = (nodes[i - 1][j]->value != 'N') ? nodes[i - 1][j] : NULL;
					nodes[i][j]->neighbors[RIGHT] = (nodes[i][j + 1]->value != 'N') ? nodes[i][j + 1] : NULL;
					nodes[i][j]->neighbors[LOWER] = (nodes[i + 1][j]->value != 'N') ? nodes[i + 1][j] : NULL;
					nodes[i][j]->neighbors[LEFT] = (nodes[i][j - 1]->value != 'N') ? nodes[i][j - 1] : NULL;
				}
			}
		}
	
	
}

void sort_heap(int i) {
	int left, right;
	struct node * temp;
	left = 2 * i + 1;
	right = 2 * i + 2;
	if (left < number_of_elements_in_heap) {
		if (min_heap[i]->actual_remoteness > min_heap[left]->actual_remoteness) {
			temp = min_heap[i];
			min_heap[i] = min_heap[left];
			min_heap[left] = temp;
			sort_heap(left);
		}
	}
	if (right < number_of_elements_in_heap) {
		if (min_heap[i]->actual_remoteness > min_heap[right]->actual_remoteness) {
			temp = min_heap[i];
			min_heap[i] = min_heap[right];
			min_heap[right] = temp;
			sort_heap(right);
		}
	}
}

void add_to_heap(struct node * v) {
	min_heap = (struct node **)realloc(min_heap, (number_of_elements_in_heap + 1) * sizeof(struct node *));
	int i = number_of_elements_in_heap;
	min_heap[number_of_elements_in_heap] = v;
	int parent = (i - 1) / 2;
	while (parent >= 0 && i > 0) {
		if (min_heap[i]->actual_remoteness < min_heap[parent]->actual_remoteness) {
			struct node * temp = min_heap[i];
			min_heap[i] = min_heap[parent];
			min_heap[parent] = temp;
		}
		i = parent;
		parent = (i - 1) / 2;
	}
	number_of_elements_in_heap++;
}

void refresh_heap(struct node * v) {
	int i;
	for (i = 0; i < number_of_elements_in_heap; i++)
		if (min_heap[i]->i == v->i && min_heap[i]->j == v->j)
			break;
	
	int parent = (i - 1) / 2;
	struct node * temp;

	while (parent >= 0 && i > 0) {
		if (min_heap[i]->actual_remoteness < min_heap[parent]->actual_remoteness) {
			temp = min_heap[i];
			min_heap[i] = min_heap[parent];
			min_heap[parent] = temp;
		}
		i = parent;
		parent = (i - 1) / 2;
	}

}

struct node * get_min_node() {
	struct node * x;
	x = min_heap[0];
	min_heap[0] = min_heap[number_of_elements_in_heap - 1];
	number_of_elements_in_heap--;
	min_heap = (struct node **)realloc(min_heap, number_of_elements_in_heap * sizeof(struct node *));
	sort_heap(0);
	x->is_in_heap = 0;
	return x;
}

void relax(struct node * u, struct node * v, int len) {
	if (v->actual_remoteness > u->actual_remoteness + len) {
		v->actual_remoteness = u->actual_remoteness + len;
		if (v->is_in_heap == 0) {
			add_to_heap(v);
			v->is_in_heap = 1;
		}
		else
			refresh_heap(v);
	}
}

void analyze_node(struct node * v) {
	int this_i = v->i;
	int this_j = v->j;
	v->flag = 1;

	for (int i = 0; i < v->number_of_neighbors; i++)
		if (v->neighbors[i] && v->neighbors[i]->flag == 0)
			relax(v, v->neighbors[i], v->neighbors[i]->time);
}

struct node * find_min_node() {
	return (number_of_elements_in_heap == 0) ? NULL : get_min_node();
}

void dijkstra(struct node * root_node) {
	
	for (int i = 0; i < height; i ++)
		for (int j = 0; j < width; j++) {
			nodes[i][j]->actual_remoteness = INT_MAX;
			nodes[i][j]->flag = 0;
			nodes[i][j]->is_in_heap = 0;
		}
	if (number_of_elements_in_heap > 0)
		free(min_heap);

	root_node->actual_remoteness = 0;
	min_heap = (struct node **)malloc(sizeof(struct node *));
	min_heap[0] = root_node;
	number_of_elements_in_heap++;
	struct node * min_node;

	while (1) {
		min_node = find_min_node();

		if (min_node)
			analyze_node(min_node);
		else
			break;
	}
}

int * add_coordinates(struct node * v, struct node * dst) {
	int check = (v->actual_remoteness == 0) ? 0 : 1;
	int * new_coordinates = NULL;
	int new_count_of_coordinates = 0;
	struct node * temp = (check == 0) ? dst : v;
	struct node * temp2 = (check == 1) ? dst : v;

	while (temp->actual_remoteness != temp2->actual_remoteness) {
		new_count_of_coordinates++;
		new_coordinates = (int *)realloc(new_coordinates, 2 * new_count_of_coordinates * sizeof(int));
		if (check == 1) {
			new_coordinates[2 * new_count_of_coordinates - 2] = temp->i;
			new_coordinates[2 * new_count_of_coordinates - 1] = temp->j;
		}
		else {
			for (int i = 2 * new_count_of_coordinates - 3; i >= 0; i--)
				new_coordinates[i + 2] = new_coordinates[i];
			
			new_coordinates[0] = temp->i;
			new_coordinates[1] = temp->j;
		}

		for (int i = 0; i < temp->number_of_neighbors; i++)
			if (temp->neighbors[i] && temp->neighbors[i]->actual_remoteness == temp->actual_remoteness - temp->time) {
				temp = temp->neighbors[i];
				break;
			}
	}

	new_count_of_coordinates++;
	new_coordinates = (int *)realloc(new_coordinates, 2 * new_count_of_coordinates * sizeof(int));
	if (check == 1) {
		new_coordinates[2 * new_count_of_coordinates - 2] = temp->i;
		new_coordinates[2 * new_count_of_coordinates - 1] = temp->j;
	}
	else {
		for (int i = 2 * new_count_of_coordinates - 3; i >= 0; i--)
			new_coordinates[i + 2] = new_coordinates[i];
		new_coordinates[0] = temp->i;
		new_coordinates[1] = temp->j;
	}

	if (coordinates) {
		coordinates = (int *)realloc(coordinates, 2 * (count_of_coordinates + new_count_of_coordinates - 1) * sizeof(int));
		for (int i = 0; i < 2 * new_count_of_coordinates - 2; i++)
			coordinates[2 * count_of_coordinates + i] = new_coordinates[i + 2];
		count_of_coordinates += new_count_of_coordinates - 1;
	}
	else {
		coordinates = (int *)realloc(coordinates, 2 * (count_of_coordinates + new_count_of_coordinates) * sizeof(int));
		for (int i = 0; i < 2 * new_count_of_coordinates; i++)
			coordinates[2 * count_of_coordinates + i] = new_coordinates[i];
		count_of_coordinates += new_count_of_coordinates;
	}
	free(new_coordinates);

	return coordinates;
}



int *save_princesses(char **map, int n, int m, int t, int *path_length)
{
	height = n;
	width = m;
	for (int i = 0; i < 6; i++)
		possible_ways[i] = 0;

	nodes = (struct node ***)malloc(height * sizeof(struct node **));
		for (int i = 0; i < height; i++) {
			nodes[i] = (struct node **)malloc(width * sizeof(struct node *));
			for (int j = 0; j < width; j++) {
				nodes[i][j] = (struct node *)malloc(sizeof(struct node));
				nodes[i][j]->actual_remoteness = INT_MAX;
				nodes[i][j]->time = (map[i][j] != 'H') ? 1 : 2;
				nodes[i][j]->flag = 0;
				nodes[i][j]->is_in_heap = 0;
				nodes[i][j]->value = map[i][j];
				nodes[i][j]->i = i;
				nodes[i][j]->j = j;
				nodes[i][j]->number_of_neighbors = 4;
				nodes[i][j]->neighbors = (struct node **)malloc(4 * sizeof(struct node *));
				for (int t = 0; t < 4; t++)
					nodes[i][j]->neighbors[t] = NULL;
				
				if (map[i][j] == 'D') {
					dragon_i = i;
					dragon_j = j;
				}
				if (map[i][j] == 'P') {
					if (p1_i == INT_MAX) {
						p1_i = i;
						p1_j = j;
					}
					else if (p2_i == INT_MAX) {
						p2_i = i;
						p2_j = j;
					}
					else if (p3_i == INT_MAX) {
						p3_i = i;
						p3_j = j;
					}
				}
				
			}
		}
	

	set_neighbors();
	
	dijkstra(nodes[p1_i][p1_j]);

	possible_ways[p1p2p3] += nodes[p2_i][p2_j]->actual_remoteness;
	possible_ways[p1p3p2] += nodes[p3_i][p3_j]->actual_remoteness;
	possible_ways[p2p1p3] += nodes[p2_i][p2_j]->actual_remoteness + nodes[p3_i][p3_j]->actual_remoteness;
	possible_ways[p2p3p1] += nodes[p3_i][p3_j]->actual_remoteness;
	possible_ways[p3p1p2] += nodes[p3_i][p3_j]->actual_remoteness + nodes[p2_i][p2_j]->actual_remoteness;
	possible_ways[p3p2p1] += nodes[p2_i][p2_j]->actual_remoteness;

	dijkstra(nodes[p3_i][p3_j]);

	possible_ways[p1p2p3] += nodes[p2_i][p2_j]->actual_remoteness;
	possible_ways[p1p3p2] += nodes[p2_i][p2_j]->actual_remoteness;
	possible_ways[p2p3p1] += nodes[p2_i][p2_j]->actual_remoteness;
	possible_ways[p3p2p1] += nodes[p2_i][p2_j]->actual_remoteness;


	dijkstra(nodes[dragon_i][dragon_j]);

	for (int i = 0; i < height; i++) {
		for (int j = 0; j < width; j++)
			if (nodes[i][j]->actual_remoteness == INT_MAX)
				printf("    ");
			else
				printf("%3.d ", nodes[i][j]->actual_remoteness);
		printf("\n");
	}

	coordinates = add_coordinates(nodes[0][0], nodes[dragon_i][dragon_j]);

	possible_ways[p1p2p3] += nodes[p1_i][p1_j]->actual_remoteness;
	possible_ways[p1p3p2] += nodes[p1_i][p1_j]->actual_remoteness;
	possible_ways[p2p1p3] += nodes[p2_i][p2_j]->actual_remoteness;
	possible_ways[p2p3p1] += nodes[p2_i][p2_j]->actual_remoteness;
	possible_ways[p3p1p2] += nodes[p3_i][p3_j]->actual_remoteness;
	possible_ways[p3p2p1] += nodes[p3_i][p3_j]->actual_remoteness;
	
	int min_index = 0;
	int min = INT_MAX;
	for (int i = 0; i < 6; i++) {
		if (possible_ways[i] < min) {
			min = possible_ways[i];
			min_index = i;
		}
	}
	
	if (min_index == p1p2p3) {
		coordinates = add_coordinates(nodes[dragon_i][dragon_j], nodes[p1_i][p1_j]);
		dijkstra(nodes[p2_i][p2_j]);
		coordinates = add_coordinates(nodes[p1_i][p1_j], nodes[p2_i][p2_j]);
		coordinates = add_coordinates(nodes[p2_i][p2_j], nodes[p3_i][p3_j]);
	}
	else if (min_index == p1p3p2) {
		coordinates = add_coordinates(nodes[dragon_i][dragon_j], nodes[p1_i][p1_j]);
		dijkstra(nodes[p3_i][p3_j]);
		coordinates = add_coordinates(nodes[p1_i][p1_j], nodes[p3_i][p3_j]);
		coordinates = add_coordinates(nodes[p3_i][p3_j], nodes[p2_i][p2_j]);
	}
	else if (min_index == p2p1p3) {
		coordinates = add_coordinates(nodes[dragon_i][dragon_j], nodes[p2_i][p2_j]);
		dijkstra(nodes[p1_i][p1_j]);
		coordinates = add_coordinates(nodes[p2_i][p2_j], nodes[p1_i][p1_j]);
		coordinates = add_coordinates(nodes[p1_i][p1_j], nodes[p3_i][p3_j]);
	}
	else if (min_index == p2p3p1) {
		coordinates = add_coordinates(nodes[dragon_i][dragon_j], nodes[p2_i][p2_j]);
		dijkstra(nodes[p3_i][p3_j]);
		coordinates = add_coordinates(nodes[p2_i][p2_j], nodes[p3_i][p3_j]);
		coordinates = add_coordinates(nodes[p3_i][p3_j], nodes[p1_i][p1_j]);
	}
	else if (min_index == p3p1p2) {
		coordinates = add_coordinates(nodes[dragon_i][dragon_j], nodes[p3_i][p3_j]);
		dijkstra(nodes[p1_i][p1_j]);
		coordinates = add_coordinates(nodes[p3_i][p3_j], nodes[p1_i][p1_j]);
		coordinates = add_coordinates(nodes[p1_i][p1_j], nodes[p2_i][p2_j]);
	}
	else if (min_index == p3p2p1) {
		coordinates = add_coordinates(nodes[dragon_i][dragon_j], nodes[p3_i][p3_j]);
		dijkstra(nodes[p2_i][p2_j]);
		coordinates = add_coordinates(nodes[p3_i][p3_j], nodes[p2_i][p2_j]);
		coordinates = add_coordinates(nodes[p2_i][p2_j], nodes[p1_i][p1_j]);
	}	

	*path_length = count_of_coordinates;
	return coordinates;
}

int main()
{
	FILE * input = fopen("C:\\Users\\user\\Desktop\\input_knight.txt", "r");
	char temp[1000];
	int width, height = 0;
	while (fscanf(input, "%s", &temp) > 0)
		height++;
	width = strlen(temp);
	
	char ** map = (char **)malloc(height * sizeof(char *));
	for (int i = 0; i < height; i++)
		map[i] = (char *)malloc(width * sizeof(char));

	rewind(input);

	char c;
	int i = 0, j = 0;
	while ((c = fgetc(input)) != EOF) {
		if (j == width) {
			i++;
			j = 0;
			continue;
		}
		map[i][j] = c;
		j++;
	}
	
	for (int i = 0; i < height; i++) {
		for (int j = 0; j < width; j++)
			printf("%c ", map[i][j]);
		printf("\n");
	}

	int path_length;
	int *path = save_princesses(map, height, width, 458, &path_length);
	
	for (int i = 0; i < path_length; i++) {
		printf("%d %d\n", path[i * 2], path[i * 2 + 1]);
		map[path[i * 2]][path[i * 2 + 1]] = ' ';
	}

	for (int i = 0; i < height; i++) {
		for (int j = 0; j < width; j++)
			printf("%c ", map[i][j]);
		printf("\n");
	}


	system("pause");
	return 0;
}