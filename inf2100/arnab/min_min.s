        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        subl    $8,%esp                 # Allocate 8 bytes
        movl    $2,%eax                 # 2
        movl    %eax,-4(%ebp)           # b being assigned
        movl    $3,%eax                 # 3
        leal    -8(%ebp),%edx           # x[index] being assigned
        popl    %ecx                    
        movl    %eax,(%edx,%ecx,4)      
.exit$main:                                
        addl    $8,%esp                 # Free 8 bytes
        popl    %ebp                    
        ret                             # end main
