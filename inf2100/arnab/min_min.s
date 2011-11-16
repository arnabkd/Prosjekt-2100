        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        subl    $8,%esp                 # Allocate 8 bytes
        movl    $2,%eax                 # 2
        movl    %eax,-4(%ebp)           # b being assigned
        movl    -4(%ebp),%eax           # Putting b in %eax
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        movl    %eax,-8(%ebp)           # c being assigned
.exit$main:                                
        addl    $8,%esp                 # Free 8 bytes
        popl    %ebp                    
        ret                             # end main
