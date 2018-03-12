#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <string.h>
#include <stdlib.h>


// размер hash-таблицы
#define HASH_TABLE_SIZE 10000

// позиция вершины дерева относительно родителя
#define LEFT_CHILD -1
#define RIGHT_CHILD 1
// позиция корня
#define ROOT 0

// узел дерева
struct node {
	char user[50];
	int balance_factor;
	int hight;
	int status;
	int quantity_of_left_children;
	struct node *parent;
	struct node *left;
	struct node *right;
};

// структура информации о сайте
struct page_inf {
	char name[50];
	struct node *users_tree;
};
#include "draw.c"
// hash-таблица
struct page_inf *table[HASH_TABLE_SIZE];

void display(struct node *vrchol);

// hash-функция
int hash(char *str) {
	int h = 0;
	int len = strlen(str);
	for (int i = 0; i < len; i++) {
		if (i % 4 == 0) h += str[i];
		else if (i % 4 == 1) h += str[i] << 8;
		else if (i % 4 == 2) h += str[i] << 16;
		else if (i % 4 == 3) h += str[i] << 24;
		h *= 73837;
	}
	return abs(h % (HASH_TABLE_SIZE));
}

void set_height(struct node *p) {
	if (!p->left && !p->right) p->hight = 1;
	else if (p->left && !p->right) p->hight = p->left->hight + 1;
	else if (!p->left && p->right) p->hight = p->right->hight + 1;
	else p->hight = ((p->left->hight > p->right->hight) ? p->left->hight : p->right->hight) + 1;
}

void set_balance_factor(struct node *p) {
	set_height(p);
	if (!p->left && !p->right) p->balance_factor = 0;
	else if (p->left && !p->right) p->balance_factor = (0 - p->left->hight);
	else if (!p->left && p->right) p->balance_factor = p->right->hight;
	else p->balance_factor = (p->right->hight - p->left->hight);
}

struct node *right_rotation(struct node *p, struct page_inf *page) {
	struct node *q = p->left;
	p->left = q->right;
	if (q->right) q->right->parent = p;
	q->right = p;
	q->parent = p->parent;
	p->parent = q;
	if (p->left) p->left->status = LEFT_CHILD;
	q->status = p->status;
	if (q->status == ROOT) page->users_tree = q;
	else if (q->status == LEFT_CHILD) q->parent->left = q;
	else q->parent->right = q;
	p->status = RIGHT_CHILD;
	p->quantity_of_left_children -= q->quantity_of_left_children + 1;
	set_balance_factor(p);
	set_balance_factor(q);
	return q;
}

struct node *left_rotation(struct node *q, struct page_inf *page) {
	struct node *p = q->right;
	q->right = p->left;
	if (p->left) p->left->parent = q;
	p->left = q;
	p->parent = q->parent;

	q->parent = p;
	if (q->right) q->right->status = RIGHT_CHILD;
	p->status = q->status;
	if (p->status == ROOT) page->users_tree = p;
	else if (p->status == LEFT_CHILD) p->parent->left = p;
	else p->parent->right = p;
	q->status = LEFT_CHILD;
	if (!q->left) q->quantity_of_left_children = 0;
	p->quantity_of_left_children += q->quantity_of_left_children + 1;
	set_balance_factor(q);
	set_balance_factor(p);
	return p;
}

struct node *balance(struct node *p, struct page_inf *page) {
	set_balance_factor(p);
	if (p->balance_factor == 2) {
		if (p->right->balance_factor < 0)
			p->right = right_rotation(p->right, page);
		return left_rotation(p, page);
	}
	if (p->balance_factor == -2) {
		if (p->left->balance_factor > 0)
			p->left = left_rotation(p->left, page);
		return right_rotation(p, page);
	}
	return p;
}

struct page_inf *find_page(char *name) {
	int index = hash(name);
	struct page_inf *page = table[index];
	
	// если данная страница сохранена, возвращаем её
	while (page) {
		if (strcmp(name, page->name) == 0)
			return page;
		if (index == HASH_TABLE_SIZE - 1) page = table[0];
		else page = table[++index];
	}
	// иначе возвращаем NULL
	return NULL;
}

struct page_inf *add_page(char *name) {
	int index = hash(name);
	struct page_inf *page = table[index];
	while (page)
		if (index == HASH_TABLE_SIZE - 1) page = table[0];
		else page = table[++index];
	table[index] = (struct page_inf *)malloc(sizeof(struct page_inf));
	page = table[index];
	strcpy(page->name, name);
	page->users_tree = NULL;
	
	return page;
}

struct node *find_min(struct node *p) {
	return (p->left) ? find_min(p->left) : p;
}

void add_user(struct page_inf *page, char *user) {
	struct node *p = page->users_tree;
	// если дерево пока не существует
	if (!p) {
		page->users_tree = (struct node *)malloc(sizeof(struct node));
		p = page->users_tree;
		p->parent = NULL;
		p->left = NULL;
		p->right = NULL;
		p->status = ROOT;
		p->quantity_of_left_children = 0;
		strcpy(p->user, user);
		balance(p, page);
	}
	// иначе
	else {
		// ищем вершину для сохранения
		while (p) {
			if (strcmp(user, p->user) == 0)
				return;
			else if (strcmp(user, p->user) < 0) {
				p->quantity_of_left_children++;
				if (!p->left) {
					p->left = (struct node *)malloc(sizeof(struct node));
					p->left->parent = p;
					p->left->status = LEFT_CHILD;
					p = p->left;
					break;
				}
				p = p->left;
			}
			else {
				if (!p->right) {
					p->right = (struct node *)malloc(sizeof(struct node));
					p->right->parent = p;
					p->right->status = RIGHT_CHILD;
					p = p->right;
					break;
				}
				p = p->right;
			}
		}
		// заполняем вершину
		p->quantity_of_left_children = 0;
		p->left = NULL;
		p->right = NULL;
		strcpy(p->user, user);
		// вычисляем фактор равновесия для вершины и всех её предков
		while (p) {
			p = balance(p, page);
			p = p->parent;
		}
	}
}

struct node *remove_min(struct node *p) {
	if (!p->left) {
		p->quantity_of_left_children = 0;
		if (p->right) p->right->status = p->status; // ... = p->stav; ... = LAVE_DIETA;
		if (p->right) p->right->parent = p->parent;
		return p->right;
	}
	p->left = remove_min(p->left);
	return p;
}

void remove_user(struct page_inf *page, char *user) {
	struct node *p = page->users_tree;
	
	// ищем user-а в дереве
	while (p) {
		if (strcmp(user, p->user) == 0)
			break;
		else if (strcmp(user, p->user) < 0) {
			p = p->left;
		}
		else
			p = p->right;
	}
	if (!p) return;
	
	// если у вершины нет потомков
	if (!p->left && !p->right) {
		if (p->status == LEFT_CHILD) {
			p = p->parent;
			free(p->left);
			p->left = NULL;
			p->quantity_of_left_children = 0;
		}
		else if (p->status == RIGHT_CHILD) {
			p = p->parent;
			free(p->right);
			p->right = NULL;
		}
		else if (p->status == ROOT){
			free(page->users_tree);
			page->users_tree = NULL;
			p = NULL;
		}
	}
	// если у вершины есть только левый потомок
	else if (p->left && !p->right) {
		struct node *son = p->left;
		if (p->status == LEFT_CHILD) {
			p->left->parent = p->parent;
			p->parent->left = p->left;
			free(p);
		}
		else if (p->status == RIGHT_CHILD) {
			p->left->parent = p->parent;
			p->parent->right = p->left;
			p->left->status = RIGHT_CHILD;
			free(p);
		}
		else {
			p->left->parent = NULL;
			p->left->status = ROOT;
			page->users_tree = p->left;
			free(p);
			p = NULL;
		}
		p = son;
	}
	// если у вершины есть только правый потомок
	else if (!p->left && p->right) {
		struct node *son = p->right;
		if (p->status == LEFT_CHILD) {
			p->right->parent = p->parent;
			p->parent->left = p->right;
			p->right->status = LEFT_CHILD;
			free(p);
		}
		else if (p->status == RIGHT_CHILD) {
			p->right->parent = p->parent;
			p->parent->right = p->right;
			free(p);
		}
		else {
			p->right->parent = NULL;
			p->right->status = ROOT;
			page->users_tree = p->right;
			free(p);
			p = NULL;
		}
		p = son;
	}
	// если у вершины есть два потомка
	else {
		struct node *left_tree = p->left;
		struct node *right_tree = p->right;
		struct node *min = find_min(right_tree); // Steeve
		struct node *parent_min = (min->status == RIGHT_CHILD) ? min : (min->right ? min->right : min->parent); // 
		int quantity_of_left_removing_tree = p->quantity_of_left_children; // 5
		min->right = remove_min(right_tree); // NULL
		//min->pravy->pocet_deti_v_lavom_strome--;

		if (min->status == RIGHT_CHILD) right_tree = right_tree->right;
		min->left = left_tree;
		left_tree->parent = min;
		
		if (p->status == LEFT_CHILD) {
			p->parent->left = min;
			min->status = LEFT_CHILD;
			min->parent = p->parent;
		}
		else if (p->status == RIGHT_CHILD) {
			p->parent->right = min;
			min->status = RIGHT_CHILD;
			min->parent = p->parent;
		}
		else {
			page->users_tree = min;
			min->status = ROOT;
			min->parent = NULL;
		}
		if (right_tree) right_tree->parent = min;
		
		free(p);
		p = min;
		p->quantity_of_left_children = quantity_of_left_removing_tree;
		p = parent_min;
	}
	while (p) {
		p = balance(p, page);
		if (p->status == LEFT_CHILD && p->parent)
			p->parent->quantity_of_left_children--;
		if (!p->left) p->quantity_of_left_children = 0;
		p = p->parent;
	}
}

void init() {
	// делаем хэш-таблицу для сайтов
	// у каждого сайта своё АВЛ-дерево
	for (int i = 0; i < HASH_TABLE_SIZE; i++)
		table[i] = NULL;
}

void like(char *name, char *user) {
	struct page_inf *page = find_page(name);
	if (!page) page = add_page(name);
	add_user(page, user);
}

void unlike(char *page, char *user) {
	struct page_inf *stranka = find_page(page);
	if (!stranka) return;
	remove_user(stranka, user);
}

char *getuser(char *name, int k) {
	struct page_inf *page = find_page(name);
	if (!page) return NULL;
	struct node *p = page->users_tree;
	if (!p) return NULL;
	
	int sum = 0;
	while (p) {
		if (k <= sum + p->quantity_of_left_children) {
			p = p->left;
		}
		else if (k == sum + p->quantity_of_left_children + 1) {
			return p->user;
		}
		else {
			sum += p->quantity_of_left_children + 1;
			p = p->right;
		}
	}
	return NULL;
}

void display(struct node *p) {
	if (!p) {
		printf("(null)\n");
		return;
	}
	printf("%s\n", p->user);
	if (p->status == LEFT_CHILD) printf("Left child\n");
	else if (p->status == RIGHT_CHILD) printf("Right child\n");
	else printf("Root\n");
	printf("BF: %d, h: %d\n", p->balance_factor, p->hight);
	printf("qolr: %d\n", p->quantity_of_left_children);
	if (p->left != NULL) printf("LC ");
	if (p->right != NULL) printf("RC");
	if (p->left || p->right) printf("\n");
	printf("---------\n");
}


#define TEST_SIZE 50
void randomtest() {
	srand(time(NULL));
	init();
	int number;
	char name[20];
	char buf[8];

	char *names[TEST_SIZE];
	for (int i = 0; i < TEST_SIZE; i++)
		names[i] = NULL;

	for (int i = 0; i < TEST_SIZE; i++) {
		number = rand() % 1000 + 1;
		strcpy(name, "id");
		_itoa(number, buf, 10);
		strcat(name, buf);
		
		int j = 0;
		while (names[j]) {
			if (strcmp(names[j], name) == 0) break;
			j++;
		}
		if (names[j]) continue;
		names[j] = (char*)malloc(20 * sizeof(char));
		strcpy(names[j], name);
		like("Star Trek", name);
		printf("like(\"Star Trek\", \"%s\");\n", name);
	}

	for (int i = 0; i < TEST_SIZE; i++) {
		number = rand() % TEST_SIZE;
		if (names[number]) {
			printf("unlike(\"Star Trek\", \"%s\");\n", names[number]);
			unlike("Star Trek", names[number]);
			free(names[number]);
			names[number] = NULL;
		}
	}
}

int main()
{
	
	randomtest();
	print_ascii_tree(table[hash("Star Trek")]->users_tree);

	system("pause");
	return 0;
}
