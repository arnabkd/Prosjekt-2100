        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        subl    $4,%esp                 # Allocate 4 bytes
        movl    $1,%eax                 # 1
        movl    %eax,-4(%ebp)           # k being assigned
        movl    -4(%ebp),%eax           # Putting k in %eax
        pushl   %eax                    
        movl    -4(%ebp),%eax           # Putting k in %eax
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        jmp     .exit$main              # return-statement for main
.exit$main:                                
        addl    $4,%esp                 # Free 4 bytes
        popl    %ebp                    
        ret                             # end main
