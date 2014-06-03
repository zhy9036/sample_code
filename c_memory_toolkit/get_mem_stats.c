//Yang Zhang, Yilin Liu
//CSE 374 Homework 6
//2014.3.13


#include "mem.h"
#include "mem_impl.h"

extern memnode* free_list;
extern uintptr_t G_TOTAL_SIZE;

//method for checking memory allocating stats
void get_mem_stats(uintptr_t* total_size, uintptr_t* total_free, uintptr_t* n_free_blocks){
	*total_size = G_TOTAL_SIZE;
	memnode* cursor = free_list;
	uintptr_t tf = 0;
	uintptr_t nfb = 0;
	while(cursor != NULL){
		nfb++;
		tf += cursor->size;
		cursor = cursor -> next;
	}
	*total_free = tf;
	*n_free_blocks = nfb;
}
