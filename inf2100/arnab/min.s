        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
.exit$main:                                
        popl    %ebp                    
        ret                             # End function main
        .data
        .globl  b
b:      .fill   4                       # int b;
        .globl  y
y:      .fill   8                       # int y[2];
        .globl  a
a:      .fill   4                       # int a;
        .globl  x
x:      .fill   8                       # int x[2];
        .text
        .globl  sum                     
sum:    pushl   %ebp                    # Start function sum
        movl    %esp,%ebp               
.exit$sum:                                
        popl    %ebp                    
        ret                             # End function sum
