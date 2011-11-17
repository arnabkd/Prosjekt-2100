        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        movl    $0,%eax                 # 0
        jmp     .exit$main              # return-statement for main
.exit$main:                                
        popl    %ebp                    
        ret                             # end main
