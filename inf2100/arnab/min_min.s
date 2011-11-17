        .globl  one                     
one:    pushl   %ebp                    # int one;
        movl    %esp,%ebp               
        movl    8(%ebp),%eax            # Putting a in %eax
        pushl   %eax                    
        movl    12(%ebp),%eax           # Putting b in %eax
        movl    %eax,%ecx               
        popl    %eax                    
        imull   %ecx,%eax               # Multiplying
        jmp     .exit$one               # return-statement for one
.exit$one:                                
        popl    %ebp                    
        ret                             # end one
        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        subl    $12,%esp                # Allocate 12 bytes
        movl    $1,%eax                 # 1
        movl    %eax,-4(%ebp)           # k being assigned
        movl    -4(%ebp),%eax           # Putting k in %eax
        pushl   %eax                    # Push parameter #1
        movl    -4(%ebp),%eax           # Putting k in %eax
        pushl   %eax                    # Push parameter #0
        call    one                     # call one
        popl    %ecx                    # Pop param #0
        popl    %ecx                    # Pop param #1
        movl    %eax,-8(%ebp)           # x being assigned
        movl    -8(%ebp),%eax           # Putting x in %eax
        pushl   %eax                    # Push parameter #1
        movl    -8(%ebp),%eax           # Putting x in %eax
        pushl   %eax                    
        movl    -4(%ebp),%eax           # Putting k in %eax
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Adding
        pushl   %eax                    # Push parameter #0
        call    one                     # call one
        popl    %ecx                    # Pop param #0
        popl    %ecx                    # Pop param #1
        pushl   %eax                    # Push parameter #1
        movl    -8(%ebp),%eax           # Putting x in %eax
        pushl   %eax                    # Push parameter #0
        call    one                     # call one
        popl    %ecx                    # Pop param #0
        popl    %ecx                    # Pop param #1
        movl    %eax,-12(%ebp)          # f being assigned
.exit$main:                                
        addl    $12,%esp                # Free 12 bytes
        popl    %ebp                    
        ret                             # end main
