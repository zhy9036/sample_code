//Yang Zhang, Yilin Liu
//CSE 374 Homework 6
//2014.3.13

#ifndef MEM_H
#define MEM_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <stdint.h>
#include <ctype.h>

#define HEAD_SIZE 16
#define DEFAULT_MEM 1024

// define a struct which can build one memory block
typedef struct memnode{
	struct memnode* next;
	uintptr_t size;
}memnode;

//pulic method headers
void* getmem(uintptr_t size);
void freemem(void* p);
void get_mem_stats(uintptr_t* total_size, uintptr_t* total_free, uintptr_t* n_free_blocks);
void print_heap(FILE* f);
#endif

