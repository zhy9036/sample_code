*******************************
* YANG ZHANG, YILIN LIU       *
* CSE374 Homework 6           *
* 3/6/2014                    *
*                             *
* README file                 *
*******************************

#1
YANG prototyped first version of makefile, getmem.c and freemem.c.
YILIN wrote tests and bench.c, get_mem_stats.c and print_heap.c.
After finished these parts, we debugged and revised the program together 

#2
Our program creates 893 blocks(1024 bytes each for body + 16 bytes each for header) time as default size;
Everytime client called getmem, the program allocated exactly the same size space needed 
with a 16 bytes header. if there's already some space in free list, use it.
Otherwise allocated new block of space. When client called freemem,
the program breaks certain memory block which is in allocated list and 
put it to the last of free list.
bench.c is a test bench. It tests the functionality as well as the performance.
Makefile is to compile all the source file and header file after certain change.

#3 
NOPE :-) :P *^_^* :D \(^o^)/ #^_^# (^.^) O(∩_∩)O (*¯︶¯*) ( ^_^ ) ( ^_^ )/~~ (>^ω^<) ╮(╯▽╰)╭   ╭∩╮  囧rz 崮rz

#4
if user’s input for n_trials is large, then the CPU running time will be longer than the 
n_trials. Also we find out the efficiency of the program is not that good as what we expected, because some times it will have some fragments, such as 16 bytes left in the free list we cannot use that. So it is not efficiency. 

#5
We followed the PDF instruction on the course web site and 
help of the instructor.


