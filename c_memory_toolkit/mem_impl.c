//Yang Zhang, Yilin Liu
//CSE 374 Homework 6
//2014.3.13
//tool functions implementation

#include "mem_impl.h"
#include "mem.h"

extern memnode* free_list;
extern memnode* allocated_list;
extern uintptr_t G_TOTAL_SIZE;
extern int allocated_list_size;

//assign a block of memory
void* allocateNew(uintptr_t actual_size){
	if(actual_size<DEFAULT_MEM){
		memnode* holder = (memnode*) malloc(DEFAULT_MEM + HEAD_SIZE);
		
		// UPTATE: G_TOTAL_SIZE
		G_TOTAL_SIZE += DEFAULT_MEM + HEAD_SIZE;
		
		memnode* used_mem = (memnode*)((uintptr_t)holder+(uintptr_t)HEAD_SIZE + DEFAULT_MEM - actual_size);
		used_mem->size = actual_size - HEAD_SIZE;
		used_mem->next = NULL;	
		holder->size = DEFAULT_MEM - actual_size;
		holder->next = NULL;
		add_to_free_list(holder);
		add_to_allocated_list(used_mem);
		return (void*) ((memnode*)((uintptr_t)used_mem + HEAD_SIZE));
	}else{
		memnode* used_mem = (memnode*) malloc(actual_size);
		
		// UPTATE: G_TOTAL_SIZE
		G_TOTAL_SIZE += actual_size;
		
		used_mem->next = NULL;
		used_mem->size = actual_size - HEAD_SIZE;
		add_to_allocated_list(used_mem);
		return (void*) ((memnode*)((uintptr_t)used_mem + HEAD_SIZE));
	}
}


//add memory block to the freelist
void add_to_free_list(memnode* p){
	if(free_list == NULL){
		p->next = NULL;
		free_list = p;
	}else{
		memnode* cur = free_list;
		while(cur->next!=NULL){
			cur = cur->next;
		}
		cur->next = p;
		cur->next->next = NULL;
	}
}

//add allocated memory to the freelist
void add_to_allocated_list(memnode* p){
	if(allocated_list == NULL){
		p->next = NULL;
		allocated_list = p;
		allocated_list_size++;
	}else{
		memnode* cur = allocated_list;
		while(cur->next!=NULL){
			cur = cur->next;
		}
		cur->next = p;
		cur->next->next = NULL;
		allocated_list_size++;

	}
}


//delete memory block from allocated memory 
void delete_from_allocate(memnode* element){
	if(allocated_list != NULL){
		if(allocated_list == element){
			allocated_list = allocated_list->next;
			allocated_list_size--;
		}else{
			memnode* cur = allocated_list;
			while(cur->next != NULL){
				if(cur->next == element){
					memnode* temp1 = element->next;
					cur->next->next = NULL;
					cur->next = temp1;
					allocated_list_size--;
					element = NULL;
					return;
				}
				cur = cur->next;
			}
		}
	}
}


//delete memory block from freelist
void delete_from_free(memnode* element){
	if(free_list != NULL){
		if(free_list == element){
			free_list = free_list->next;
		}else{
			memnode* cur = free_list;
			while(cur->next != NULL){
				if(cur->next == element){
					memnode* temp1 = element->next;
					cur->next->next = NULL;
					cur->next = temp1;
					element = NULL;
					return;
				}
				cur = cur->next;
			}
		}
	}
}




