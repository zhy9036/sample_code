//Yang Zhang, Yilin Liu
//CSE 374 Homework 6
//2014.3.13

#include "mem.h"
#include "mem_impl.h"

extern memnode* free_list;
extern memnode* allocated_list;
int allocated_list_size = 0;

// method for release allocated memory block
void freemem(void* p){
	if(p == NULL){
		return;
	}

	//case 1: check if p is adjacent to one of the free_list node
	memnode* cursor = free_list;
	while(cursor != NULL){
		if((memnode*)((uintptr_t)cursor + HEAD_SIZE + cursor->size + HEAD_SIZE) == (memnode*)p){
			cursor -> size = cursor -> size + ((memnode*)((uintptr_t)p - HEAD_SIZE))->size + HEAD_SIZE;
			//delete p from ALA;
			delete_from_allocate(((memnode*)((uintptr_t)p- HEAD_SIZE)));
			return; 
		}
		cursor = cursor->next;
	}
	//delete p from ALA;
	delete_from_allocate(((memnode*)((uintptr_t)p- HEAD_SIZE)));
	add_to_free_list((memnode*)((uintptr_t)p- HEAD_SIZE));
}


