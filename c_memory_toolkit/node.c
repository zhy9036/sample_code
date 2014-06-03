//Yang Zhang, Yilin Liu
//CSE 374 Homework 6
//2014.3.13

#include "mem_impl.h"

// method for initializing a new memory block
memnode* ini_node(uintptr_t s){
	memnode* n = (memonode*) malloc(s);
	n->size=s;
	return n;
}