        .globl  one                     
one:    pushl   %ebp                    # int one;
        movl    %esp,%ebp               
        movl    $1,%eax                 # 1
        jmp     .exit$one               # return-statement for one
.exit$one:                                
        popl    %ebp                    
        ret                             # end one
        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        subl    $4,%esp                 # Allocate 4 bytes
        movl    $2,%eax                 # 2
        pushl   %eax                    # Push parameter #1
        movl    $1,%eax                 # 1
        pushl   %eax                    # Push parameter #0
        call    one                     # call one
        popl    %ecx                    # Pop param #0
        popl    %ecx                    # Pop param #1
        movl    %eax,-4(%ebp)           # k being assigned
.exit$main:                                
        addl    $4,%esp                 # Free 4 bytes
        popl    %ebp                    
        ret                             # end main
