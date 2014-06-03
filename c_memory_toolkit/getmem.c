//Yang Zhang, Yilin Liu
//CSE 374 Homework 6
//2014.3.13

#include "mem.h"
#include "mem_impl.h"


uintptr_t G_TOTAL_SIZE = 0;
memnode* free_list = NULL;
memnode* allocated_list = NULL;

//method for allocating memory for memory block
void* getmem(uintptr_t size){
	if(size<=0){
		return NULL;
	}
	uintptr_t actual_size = (size % HEAD_SIZE != 0) ? (size+HEAD_SIZE) + (HEAD_SIZE - (size % HEAD_SIZE)) : (size+HEAD_SIZE); 

	if(free_list == NULL){
		
		return allocateNew(actual_size);
	}

	memnode* cur = free_list;

	// first add header(16 bytes) and then make it a mutiple of 16

	while(cur != NULL){

		if(cur->size > actual_size){ //check if there is enough block for new request

			memnode* newMem = (memnode*)((uintptr_t)cur + HEAD_SIZE + cur->size - actual_size);
			
			newMem->size = actual_size - HEAD_SIZE;
			newMem->next = NULL;
			cur->size = cur->size - actual_size;

			add_to_allocated_list(newMem);
			return (void*) ((memnode*) ((uintptr_t)newMem+HEAD_SIZE));
		}else if(cur->size == actual_size){
			//delete cur from free_list and add it to the AMA
			delete_from_free(cur);
			add_to_allocated_list(cur);

			return (void*) ((memnode*) ((uintptr_t)cur+HEAD_SIZE));
		}
		cur = cur->next;
	}
	return allocateNew(actual_size);
}

