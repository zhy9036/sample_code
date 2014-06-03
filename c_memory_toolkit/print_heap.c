//Yang Zhang, Yilin Liu
//CSE 374 Homework 6
//2014.3.13

#include "mem.h"

extern memnode* free_list;

//method for printing heap info
void print_heap(FILE* f){
	// check input file validity
	if(f == NULL){
		printf("File Error!\n");
		return;
	}
	memnode* cursor = free_list;

	while(cursor != NULL){
		fprintf(f, "address: %p length: %d \n", cursor, (int) cursor->size);
		cursor = cursor -> next;
	}
}
