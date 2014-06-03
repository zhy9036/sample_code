/*

CSE 374 HW6
YANG ZHANG, YILIN LIU

*/

#ifndef MEM_IMPL_H
#define MEM_IMPL_H

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
#include <stdint.h>
#include "mem.h"

 void* allocateNew(uintptr_t actual_size);
 void add_to_free_list(memnode* p);
 void add_to_allocated_list(memnode* p);
 void delete_from_allocate(memnode* element);
 void delete_from_free(memnode* element);



#endif
