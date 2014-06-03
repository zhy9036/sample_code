

# CSE 374 HW6
# YANG ZHANG, YILIN LIU


bench:  bench.o get_mem_stats.o freemem.o getmem.o print_heap.o mem_impl.o 
		gcc -Wall -g -o bench bench.o freemem.o get_mem_stats.o getmem.o print_heap.o mem_impl.o

freemem.o: freemem.c mem.h mem_impl.h
	gcc -Wall -g -c freemem.c

get_mem_stats.o: get_mem_stats.c mem_impl.h
	gcc -Wall -g -c get_mem_stats.c

print_heap.o: print_heap.c mem_impl.h
	gcc -Wall -g -c print_heap.c

getmem.o: getmem.c mem.h mem_impl.h
	gcc -Wall -g -c getmem.c 

bench.o: bench.c mem.h mem_impl.h
	gcc -Wall -g -c bench.c	

mem_impl.o: mem_impl.c mem.h mem_impl.h
	gcc -Wall -g -c mem_impl.c	

test:
	./bench

clean:
	rm *.o bench

dist:
	rm svn.log
	svn log > svn.log
	tar -cf hw6.tar *.c *.h README.txt svn.log Makefile