        .data
        .globl  e
e:      .fill   8                       # int e[2];
        .text
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
.exit$main:                                
        popl    %ebp                    
        ret                             # End function main
