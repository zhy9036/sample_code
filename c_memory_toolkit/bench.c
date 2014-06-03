//Yang Zhang, Yilin Liu
//CSE 374 Homework 6
//2014.3.13
//benchmark

#include <time.h>
#include "mem.h"
#include "mem_impl.h"

#define ntrials 10000

#define pctget 50 

#define pctlarge 10

#define small_limit 200

#define large_limit 20000


extern memnode* free_list;
extern memnode* allocated_list;
extern int allocated_list_size;
void setup(int* paramaters, int argc, char** argv);

//select running freemen or getmem
int makechoice(int pct){
	int result = rand()%100;
	/*
	* return 1 freemem or select small  
	* return 0 getmem or select large
	*/
	return (result>pct)? 1 : 0; 
}

//select small or large size
int gen_size(int mode, int limit_l, int limit_h){

	if(mode){ // return small size
		return 1 + (rand())% (limit_l-1);
	}else{ // return large size
		return 1 + limit_l + (rand())% (limit_h - limit_l);
	}
}

//randomly free an element from allocated memory list
memnode* random_free(){
	//printf("======= random_free\n");
	memnode* cur = allocated_list;
	if(cur != NULL){
		//printf("allo size: %d\n", allocated_list_size);
		int index = (rand())%allocated_list_size;
		int i;
		for(i = 0; i<index; i++){
			cur = cur->next;

			//printf("cur->size %lu \n", cur -> size);
		}
		memnode* p = (void*)((memnode*) ((uintptr_t)cur + HEAD_SIZE));
		freemem(p);
	}
	return cur;
}

// the test program with defined testing paramaters
int main(int argc, char** argv){
	clock_t start, stop;
	double cpu_time;
	start = clock();
	time_t t;
	FILE* f = fopen("log","w");
	int paramaters[6] = {ntrials, pctget, pctlarge, small_limit, large_limit, -1};
	setup(paramaters, argc, argv);
	if(paramaters[5] < 0) srand((unsigned)time(&t));
	else srand((unsigned)paramaters[5]);
	int i;
	
	for(i=0; i<paramaters[0]; i++){
		int function_choice = makechoice(paramaters[1]);
		int size_choice = makechoice(paramaters[2]);
		int small = paramaters[3];
		int large = paramaters[4];
		if(function_choice){
			int size = gen_size(size_choice, small, large);
			getmem(size);
		}else{
			random_free();
		}
	}
	print_heap(f);
	uintptr_t *total_size = (uintptr_t *)malloc(sizeof(uintptr_t));
	uintptr_t *total_free = (uintptr_t *)malloc(sizeof(uintptr_t));
	uintptr_t *n_free_blocks = (uintptr_t *)malloc(sizeof(uintptr_t));
	// get and print the mem stats to file 
	get_mem_stats(total_size, total_free, n_free_blocks);
	stop = clock();
    cpu_time = ((double) (stop - start)) / CLOCKS_PER_SEC;
    fprintf(f,"cpu_time: %f  seconds\n",cpu_time);
    fprintf(f,"total_size: %lu bytes\n",*total_size);
//    printf("n_free_blocks: %lu blocks\n",*n_free_blocks);
    fprintf(f,"n_free_blocks: %lu blocks\n",*n_free_blocks);
    fprintf(f,"total_free size :%lu bytes\n",*total_free);
	fclose(f);
	free(total_size);
	free(total_free);
	free(n_free_blocks);
	return 0;
}

void setup(int* paramaters, int argc, char** argv){
	int i;
	for(i=1; i<argc; i++){
		paramaters[i-1] = atoi(argv[i]); 
	}
}

