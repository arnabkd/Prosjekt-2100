        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
.exit$main:
        popl    %ebp                    
        ret                             # end main
