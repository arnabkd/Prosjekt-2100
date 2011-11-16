        .data
        .globl  a
a:      .fill   4                       # int a;
        .globl  x
x:      .fill   8                       # int x[2];
        .text
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
.exit$main:                                
        popl    %ebp                    
        ret                             # End function main
